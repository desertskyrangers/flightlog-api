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
public class DashboardService extends CommonDashboardService<Dashboard> implements DashboardServices {

	public DashboardService( AircraftServices aircraftServices, FlightServices flightServices, UserServices userServices, StateRetrieving stateRetrieving, StatePersisting statePersisting ) {
		super( aircraftServices, flightServices, userServices, stateRetrieving, statePersisting );
		flightServices.setDashboardServices( this );
		userServices.setDashboardServices( this );
	}

	@Override
	public Optional<Dashboard> find( UUID uuid ) {
		return Optional.empty();
	}

	@Override
	public Optional<Dashboard> findByUser( User user ) {
		return getStateRetrieving().findDashboard( user );
	}

	@Override
	public Dashboard upsert( User user, Dashboard dashboard ) {
		return getStatePersisting().upsertDashboard( user, dashboard );
	}

	@Override
	public String update( User user ) {
		// Assign a user dashboard id if one does not exist
		if( user.dashboardId() == null ) getStatePersisting().upsert( user.dashboardId( UUID.randomUUID() ) );

		Map<String, Object> preferences = getUserServices().getPreferences( user );

		boolean showObserverStats = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.SHOW_OBSERVER_STATS ) ) );
		boolean showAircraftStats = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.SHOW_AIRCRAFT_STATS ) ) );

		List<Aircraft> aircraft;
		// FIXME What if the dashboard is public???
		if( "true".equals( String.valueOf( preferences.get( PreferenceKey.SHOW_ALL_AIRCRAFT ) ) ) ) {
			aircraft = getAircraftServices().findAllByOwner( user.id() );
		} else {
			aircraft = getAircraftServices().findAllByOwnerAndStatus( user.id(), AircraftStatus.AIRWORTHY );
		}

		// Collect the aircraft statistics
		List<AircraftStats> aircraftStats = aircraft.stream().map( a -> {
			AircraftStats stats = new AircraftStats();
			stats.setId( a.id().toString() );
			stats.setName( a.name() );
			stats.setType( a.type().name().toLowerCase() );
			stats.setLastFlightTimestamp( getFlightServices().getLastAircraftFlight( a ).map( Flight::timestamp ).orElse( -1L ) );
			stats.setFlightCount( getFlightServices().getAircraftFlightCount( a ) );
			stats.setFlightTime( getFlightServices().getAircraftFlightTime( a ) );
			return stats;
		} ).toList();

		// Create a map for the dashboard
		Map<String, Object> map = new HashMap<>();
		map.put( "displayName", user.name() );
		map.put( "pilotFlightCount", String.valueOf( getFlightServices().getPilotFlightCount( user.id() ) ) );
		map.put( "pilotFlightTime", String.valueOf( getFlightServices().getPilotFlightTime( user.id() ) ) );
		if( showObserverStats ) {
			map.put( "observerFlightCount", String.valueOf( getFlightServices().getObserverFlightCount( user.id() ) ) );
			map.put( "observerFlightTime", String.valueOf( getFlightServices().getObserverFlightTime( user.id() ) ) );
		}
		map.put( "lastPilotFlightTimestamp", getFlightServices().getLastPilotFlight( user ).map( Flight::timestamp ).orElse( -1L ) );
		if( showAircraftStats && aircraftStats.size() > 0 ) map.put( "aircraftStats", aircraftStats );

		log.warn( "storing {} -> {}", user.dashboardId(), Json.stringify( map ));

		return getStatePersisting().upsertProjection( user.dashboardId(), Json.stringify( map ) );
	}

	//@Override
	public String update( Group group ) {
		// Assign a group dashboard id if one does not exist
		if( group.dashboardId() == null ) group = getStatePersisting().upsert( group.dashboardId( UUID.randomUUID() ) );

		// For each user in the group find their pilot flight count and time
		AtomicLong pilotFlightCount = new AtomicLong( 0 );
		AtomicLong pilotFlightTime = new AtomicLong( 0 );
		AtomicLong observerFlightCount = new AtomicLong( 0 );
		AtomicLong observerFlightTime = new AtomicLong( 0 );
		group.users().forEach( u -> {
			pilotFlightCount.addAndGet( getFlightServices().getPilotFlightCount( u.id() ) );
			pilotFlightTime.addAndGet( getFlightServices().getPilotFlightTime( u.id() ) );
			observerFlightCount.addAndGet( getFlightServices().getObserverFlightCount( u.id() ) );
			observerFlightTime.addAndGet( getFlightServices().getObserverFlightTime( u.id() ) );
		} );

		// Create a map for the dashboard
		Map<String, Object> map = new HashMap<>();
		map.put( "displayName", group.name() );
		map.put( "pilotFlightCount", String.valueOf( pilotFlightCount ) );
		map.put( "pilotFlightTime", String.valueOf( pilotFlightTime ) );
		map.put( "observerFlightCount", String.valueOf( observerFlightCount ) );
		map.put( "observerFlightTime", String.valueOf( observerFlightTime ) );

		return getStatePersisting().upsertProjection( group.dashboardId(), Json.stringify( map ) );
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
