package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import com.desertskyrangers.flightdeck.util.Json;
import com.desertskyrangers.flightdeck.util.StatCollector;
import com.desertskyrangers.flightdeck.util.StatSeries;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
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
	//@Async
	public Future<String> update( User user ) {
		// Assign dashboard ids if they do not exist
		if( user.dashboardId() == null ) user.dashboardId( UUID.randomUUID() );
		if( user.publicDashboardId() == null ) user.publicDashboardId( UUID.randomUUID() );
		statePersisting.upsert( user );

		// Update the user dashboards
		update( user, user.publicDashboardId(), true );
		return update( user, user.dashboardId(), false );
	}

	private Future<String> update( User user, UUID id, boolean isPublic ) {
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
			stats.setId( a.id() );
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

		//log.warn( "user " + (isPublic ? "public" : "private") + " dashboard {} -> {}", id, Json.stringify( map ) );

		return new AsyncResult<>( statePersisting.upsertProjection( id, Json.stringify( map ) ) );
	}

	@Override
	//@Async
	public Future<String> update( final Group group ) {
		// Assign a group dashboard id if one does not exist
		if( group.dashboardId() == null ) statePersisting.upsert( group.dashboardId( UUID.randomUUID() ) );

		// For each user in the group find their pilot flight count and time
		StatCollector<User> collector = new StatCollector<>();
		group.users().forEach( u -> {
			collector.add( "pilotFlightCount", u, flightServices.getPilotFlightCount( u ) );
			collector.add( "pilotFlightTime", u, flightServices.getPilotFlightTime( u ) );
			collector.add( "observerFlightCount", u, flightServices.getObserverFlightCount( u ) );
			collector.add( "observerFlightTime", u, flightServices.getObserverFlightTime( u ) );
		} );

		// Collect the member statistics
		List<MemberStats> memberStats = group.users().stream().sorted().map( user -> {
			Map<String, Object> preferences = userServices.getPreferences( user );
			boolean isPublicDashboardEnabled = Boolean.parseBoolean( String.valueOf( preferences.get( PreferenceKey.ENABLE_PUBLIC_DASHBOARD ) ) );

			MemberStats stats = new MemberStats();
			stats.setId( user.id() );
			stats.setName( user.name() );
			stats.setFlightCount( flightServices.getPilotFlightCount( user ) );
			stats.setFlightTime( flightServices.getPilotFlightTime( user ) );
			stats.setLastFlightTimestamp( flightServices.getLastPilotFlight( user ).map( Flight::timestamp ).orElse( -1L ) );
			stats.setPublicDashboardEnabled( isPublicDashboardEnabled );
			return stats;
		} ).toList();

		// NEXT Group member record book (leader board?)
		// Pilot with the highest total flight count (from memberStats)
		StatSeries<User> pilotWithHighestTotalFlightCount = collector.get( "pilotFlightCount" );
		// Pilot with the highest total flight time (from memberStats)
		StatSeries<User> pilotWithHighestTotalFlightTime = collector.get( "pilotFlightTime" );
		// Pilot with the longest individual flight time [this is a flight event] (what aircraft, what date, what location)
		// Pilot who flew most recently (from memberStats)

		log.warn( "Pilot with most flights = " + pilotWithHighestTotalFlightCount.getMaxValueOwner().name() );
		log.warn( "Pilot with most time = " + pilotWithHighestTotalFlightTime.getMaxValueOwner().name() );

		// NEXT Group aircraft record book (leader board?)
		// Aircraft with highest total flight count
		// Aircraft with highest total flight time
		// Aircraft with longest single flight time [this is a historical event] (what pilot, what date, what location)
		// Aircraft that flew most recently

		// Create a map for the dashboard
		Map<String, Object> map = new HashMap<>();
		map.put( "displayName", group.name() );
		map.put( "pilotFlightCount", String.valueOf( (int)collector.get( "pilotFlightCount" ).getSum() ) );
		map.put( "pilotFlightTime", String.valueOf( (long)collector.get( "pilotFlightTime" ).getSum() ) );
		map.put( "observerFlightCount", String.valueOf( (int)collector.get( "observerFlightCount" ).getSum() ) );
		map.put( "observerFlightTime", String.valueOf( (long)collector.get( "observerFlightTime" ).getSum() ) );
		if( memberStats.size() > 0 ) map.put( "memberStats", memberStats );

		map.put(
			"pilotWithHighestTotalFlightCount",
			new Record(
				"Highest flight count",
				"The pilot with the highest total flight count across all flights the pilot has flown",
				Integer.toString( (int)pilotWithHighestTotalFlightCount.getMax() ),
				"count",
				pilotWithHighestTotalFlightCount.getMaxValueOwner().name()
			)
		);
		map.put(
			"pilotWithHighestTotalFlightTime",
			new Record(
				"Most flight time",
				"The pilot with the highest total flight time across all flights the pilot has flown",
				Integer.toString( (int)pilotWithHighestTotalFlightTime.getMax() ),
				"flight-time",
				pilotWithHighestTotalFlightTime.getMaxValueOwner().name()
			)
		);


		return new AsyncResult<>( statePersisting.upsertProjection( group.dashboardId(), Json.stringify( map ) ) );
	}

	/**
	 * This class is used to store the member statistics in a projection. It must
	 * conform to the bean specification.
	 */
	@Data
	@JsonSerialize
	@Accessors( chain = true )
	private static class AircraftStats {

		private UUID id;

		private String name;

		private String type;

		private Integer flightCount;

		private Long flightTime;

		private Long lastFlightTimestamp;

	}

	/**
	 * This class is used to store the member statistics in a projection. It must
	 * conform to the bean specification.
	 */
	@Data
	@JsonSerialize
	@Accessors( chain = true )
	private static class MemberStats {

		private UUID id;

		private String name;

		private Integer flightCount;

		private Long flightTime;

		private Long lastFlightTimestamp;

		private boolean publicDashboardEnabled;

	}

	private record Record(String name, String description, String value, String type, String owner) {}

}
