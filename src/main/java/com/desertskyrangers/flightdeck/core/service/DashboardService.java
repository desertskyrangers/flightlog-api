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
		// NEXT Continue here with creating the public dashboard projection
		//update( user, true );
		return update( user, false );
	}

	private String update( User user, boolean isPublic ) {
		// Assign a user dashboard id if one does not exist
		if( user.dashboardId() == null ) statePersisting.upsert( user.dashboardId( UUID.randomUUID() ) );

		Map<String, Object> preferences = userServices.getPreferences( user );

		boolean showObserverStats = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.SHOW_OBSERVER_STATS ) ) );
		boolean showAircraftStats = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.SHOW_AIRCRAFT_STATS ) ) );

		List<Aircraft> aircraft;
		// FIXME What if the dashboard is public???
		if( "true".equals( String.valueOf( preferences.get( PreferenceKey.SHOW_ALL_AIRCRAFT ) ) ) ) {
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
			stats.setLastFlightTimestamp( flightServices.getLastAircraftFlight( a ).map( Flight::timestamp ).orElse( -1L ) );
			stats.setFlightCount( flightServices.getAircraftFlightCount( a ) );
			stats.setFlightTime( flightServices.getAircraftFlightTime( a ) );
			return stats;
		} ).toList();

		// Create a map for the dashboard
		Map<String, Object> map = new HashMap<>();
		map.put( "displayName", user.name() );
		map.put( "pilotFlightCount", String.valueOf( flightServices.getPilotFlightCount( user.id() ) ) );
		map.put( "pilotFlightTime", String.valueOf( flightServices.getPilotFlightTime( user.id() ) ) );
		if( showObserverStats ) {
			map.put( "observerFlightCount", String.valueOf( flightServices.getObserverFlightCount( user.id() ) ) );
			map.put( "observerFlightTime", String.valueOf( flightServices.getObserverFlightTime( user.id() ) ) );
		}
		map.put( "lastPilotFlightTimestamp", flightServices.getLastPilotFlight( user ).map( Flight::timestamp ).orElse( -1L ) );
		if( showAircraftStats && aircraftStats.size() > 0 ) map.put( "aircraftStats", aircraftStats );

		//log.warn( "storing {} -> {}", user.dashboardId(), Json.stringify( map ));

		return statePersisting.upsertProjection( user.dashboardId(), Json.stringify( map ) );
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
		group.users().forEach( u -> {
			pilotFlightCount.addAndGet( flightServices.getPilotFlightCount( u.id() ) );
			pilotFlightTime.addAndGet( flightServices.getPilotFlightTime( u.id() ) );
			observerFlightCount.addAndGet( flightServices.getObserverFlightCount( u.id() ) );
			observerFlightTime.addAndGet( flightServices.getObserverFlightTime( u.id() ) );
		} );

		// Create a map for the dashboard
		Map<String, Object> map = new HashMap<>();
		map.put( "displayName", group.name() );
		map.put( "pilotFlightCount", String.valueOf( pilotFlightCount ) );
		map.put( "pilotFlightTime", String.valueOf( pilotFlightTime ) );
		map.put( "observerFlightCount", String.valueOf( observerFlightCount ) );
		map.put( "observerFlightTime", String.valueOf( observerFlightTime ) );

		return statePersisting.upsertProjection( group.dashboardId(), Json.stringify( map ) );
	}

	@Data
	@Accessors( chain = true)
	private static class AircraftStats {

		private String id;

		private String name;

		private String type;

		private Long lastFlightTimestamp;

		private Integer flightCount;

		private Long flightTime;

	}

}
