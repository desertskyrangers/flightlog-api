package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import com.desertskyrangers.flightdeck.util.Json;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class DashboardService implements DashboardServices {

	private final FlightServices flightServices;

	private final AircraftServices aircraftServices;

	private final UserServices userServices;

	private final StateRetrieving stateRetrieving;

	private final StatePersisting statePersisting;

	public DashboardService( AircraftServices aircraftServices, FlightServices flightServices, UserServices userServices, StateRetrieving stateRetrieving, StatePersisting statePersisting ) {
		this.aircraftServices = aircraftServices;
		this.flightServices = flightServices;
		this.userServices = userServices;
		this.stateRetrieving = stateRetrieving;
		this.statePersisting = statePersisting;
		flightServices.setDashboardServices( this );
		userServices.setDashboardServices( this );
	}

	@Override
	public String update( User user ) {
		// Assign dashboard ids if they do not exist
		if( user.dashboardId() == null ) user.dashboardId( UUID.randomUUID() );
		if( user.publicDashboardId() == null ) user.publicDashboardId( UUID.randomUUID() );
		statePersisting.upsert( user );

		// Update the user dashboards
		update( user, user.publicDashboardId(), true );
		return update( user, user.dashboardId(), false );
	}

	private String update( User user, UUID id, boolean isPublic ) {
		Map<String, Object> preferences = userServices.getPreferences( user );

		boolean showPrivateObserverStats = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.SHOW_OBSERVER_STATS ) ) );
		boolean showPrivateAircraftStats = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.SHOW_AIRCRAFT_STATS ) ) );
		boolean showPrivateAllAircraft = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.SHOW_ALL_AIRCRAFT ) ) );
		boolean showPublicObserverStats = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.SHOW_PUBLIC_OBSERVER_STATS ) ) );
		boolean showPublicAircraftStats = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.SHOW_PUBLIC_AIRCRAFT_STATS ) ) );
		boolean showPublicAllAircraft = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.SHOW_PUBLIC_ALL_AIRCRAFT ) ) );

		boolean showObserverStats = isPublic ? showPublicObserverStats : showPrivateObserverStats;
		boolean showAircraftStats = isPublic ? showPublicAircraftStats : showPrivateAircraftStats;
		boolean showAllAircraft = isPublic ? showPublicAllAircraft : showPrivateAllAircraft;

		List<Aircraft> aircraft;
		if( showAllAircraft ) {
			aircraft = aircraftServices.findAllByOwner( user.id() );
		} else {
			aircraft = aircraftServices.findAllByOwnerAndStatus( user.id(), AircraftStatus.AIRWORTHY );
		}

		// Collect the aircraft statistics
		List<AircraftStats> aircraftStats = aircraft.stream().map( a -> {
			AircraftStats stats = new AircraftStats();
			stats.setId( a.id().toString() );
			stats.setName( a.name() );
			stats.setType( a.type().name().toLowerCase() );
			stats.setFlightCount( flightServices.getAircraftFlightCount( a ) );
			stats.setFlightTime( flightServices.getAircraftFlightTime( a ) );
			stats.setLastFlightTimestamp( flightServices.getLastAircraftFlight( a ).map( Flight::timestamp ).orElse( -1L ) );
			return stats;
		} ).toList();

		// Create a map for the dashboard
		Map<String, Object> map = new HashMap<>();
		map.put( "displayName", user.name() );
		map.put( "pilotFlightCount", String.valueOf( flightServices.getPilotFlightCount( user ) ) );
		map.put( "pilotFlightTime", String.valueOf( flightServices.getPilotFlightTime( user ) ) );
		if( showObserverStats ) {
			map.put( "observerFlightCount", String.valueOf( flightServices.getObserverFlightCount( user ) ) );
			map.put( "observerFlightTime", String.valueOf( flightServices.getObserverFlightTime( user ) ) );
		}
		map.put( "lastPilotFlightTimestamp", flightServices.getLastPilotFlight( user ).map( Flight::timestamp ).orElse( -1L ) );
		if( showAircraftStats && aircraftStats.size() > 0 ) map.put( "aircraftStats", aircraftStats );

		log.warn( "user " + (isPublic ? "public" : "private") + " dashboard {} -> {}", id, Json.stringify( map ) );

		return statePersisting.upsertProjection( id, Json.stringify( map ) );
	}

	//@Override
	public String update( Group group ) {
		// Assign a group dashboard id if one does not exist
		if( group.dashboardId() == null ) group = statePersisting.upsert( group.dashboardId( UUID.randomUUID() ) );

		// For each user in the group find their pilot flight count and time
		AtomicLong pilotFlightCount = new AtomicLong( 0 );
		AtomicLong pilotFlightTime = new AtomicLong( 0 );
		AtomicLong observerFlightCount = new AtomicLong( 0 );
		AtomicLong observerFlightTime = new AtomicLong( 0 );
		// FIXME Sort users
		group.users().forEach( u -> {
			pilotFlightCount.addAndGet( flightServices.getPilotFlightCount( u ) );
			pilotFlightTime.addAndGet( flightServices.getPilotFlightTime( u ) );
			observerFlightCount.addAndGet( flightServices.getObserverFlightCount( u ) );
			observerFlightTime.addAndGet( flightServices.getObserverFlightTime( u ) );
		} );

		// Collect the member statistics
		List<MemberStats> memberStats = group.users().stream().map( user -> {
			Map<String, Object> preferences = userServices.getPreferences( user );
			boolean isPublicDashboardEnabled = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.ENABLE_PUBLIC_DASHBOARD ) ) );

			MemberStats stats = new MemberStats();
			stats.setId( user.id().toString() );
			stats.setName( user.name() );
			stats.setFlightCount( flightServices.getPilotFlightCount( user ) );
			stats.setFlightTime( flightServices.getPilotFlightTime( user ) );
			stats.setLastFlightTimestamp( flightServices.getLastPilotFlight( user ).map( Flight::timestamp ).orElse( -1L ) );
			stats.setPublicDashboardEnabled( isPublicDashboardEnabled );
			return stats;
		} ).toList();

		// Create a map for the dashboard
		Map<String, Object> map = new HashMap<>();
		map.put( "displayName", group.name() );
		map.put( "pilotFlightCount", String.valueOf( pilotFlightCount ) );
		map.put( "pilotFlightTime", String.valueOf( pilotFlightTime ) );
		map.put( "observerFlightCount", String.valueOf( observerFlightCount ) );
		map.put( "observerFlightTime", String.valueOf( observerFlightTime ) );
		if( memberStats.size() > 0 ) map.put( "memberStats", memberStats );

		return statePersisting.upsertProjection( group.dashboardId(), Json.stringify( map ) );
	}

	@Data
	@Accessors( chain = true )
	private static class AircraftStats {

		private String id;

		private String name;

		private String type;

		private Integer flightCount;

		private Long flightTime;

		private Long lastFlightTimestamp;

	}

	@Data
	@Accessors( chain = true )
	private static class MemberStats {

		private String id;

		private String name;

		private Integer flightCount;

		private Long flightTime;

		private Long lastFlightTimestamp;

		private boolean publicDashboardEnabled;

	}

}
