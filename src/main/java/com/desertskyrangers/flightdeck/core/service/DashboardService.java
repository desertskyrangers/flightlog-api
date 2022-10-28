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
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

@Service
@Slf4j
public class DashboardService implements DashboardServices {

	public static final String GROUPS = "groups";

	public static final String DISPLAY_NAME = "displayName";

	public static final String PILOT_FLIGHT_COUNT = "pilotFlightCount";

	public static final String PILOT_FLIGHT_TIME = "pilotFlightTime";

	public static final String OBSERVER_FLIGHT_COUNT = "observerFlightCount";

	public static final String OBSERVER_FLIGHT_TIME = "observerFlightTime";

	public static final String PILOT_FLIGHT_DATE = "pilotFlightDate";

	public static final String MEMBER_STATS = "memberStats";

	public static final String PILOT_LONGEST_FLIGHT = "pilotLongestFlight";

	public static final String PILOT_LAST_FLIGHT_TIMESTAMP = "pilotLastFlightTimestamp";

	public static final String AIRCRAFT_STATS = "aircraftStats";

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
		aircraftServices.setDashboardServices( this );
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
			stats.setStatus( a.status().name().toLowerCase() );
			if( a.baseColor() != null ) stats.setBaseColor( a.baseColor().toWeb() );
			if( a.trimColor() != null ) stats.setTrimColor( a.trimColor().toWeb() );
			stats.setFlightCount( flightServices.getAircraftFlightCount( a ) );
			stats.setFlightTime( flightServices.getAircraftFlightTime( a ) );
			stats.setLastFlightTimestamp( flightServices.getLastAircraftFlight( a ).map( Flight::timestamp ).orElse( -1L ) );
			return stats;
		} ).toList();

		// Create a map for the dashboard
		Map<String, Object> map = new HashMap<>();
		map.put( GROUPS, user.groups().stream().sorted().map( this::toGroupProjection ).toList() );
		map.put( DISPLAY_NAME, user.name() );
		map.put( PILOT_FLIGHT_COUNT, String.valueOf( flightServices.getPilotFlightCount( user ) ) );
		map.put( PILOT_FLIGHT_TIME, String.valueOf( flightServices.getPilotFlightTime( user ) ) );
		if( showObserverStats ) {
			map.put( OBSERVER_FLIGHT_COUNT, String.valueOf( flightServices.getObserverFlightCount( user ) ) );
			map.put( OBSERVER_FLIGHT_TIME, String.valueOf( flightServices.getObserverFlightTime( user ) ) );
		}
		map.put( PILOT_LAST_FLIGHT_TIMESTAMP, flightServices.getLastPilotFlight( user ).map( Flight::timestamp ).orElse( -1L ) );
		if( showAircraftStats && aircraftStats.size() > 0 ) map.put( AIRCRAFT_STATS, aircraftStats );

		//log.warn( "user " + (isPublic ? "public" : "private") + " dashboard {} -> {}", id, Json.stringify( map ) );

		return new AsyncResult<>( statePersisting.upsertProjection( id, Json.stringify( map ) ) );
	}

	private Map<String,Object> toGroupProjection( Group group ) {
		Map<String,Object> map = new HashMap<>();

		map.put( "id", group.id().toString() );
		map.put( "name", group.name() );
		map.put( "type", group.type().name().toLowerCase() );

		return map;
	}

	@Override
	//@Async
	public Future<String> update( final Group group ) {
		// Assign a group dashboard id if one does not exist
		if( group.dashboardId() == null ) statePersisting.upsert( group.dashboardId( UUID.randomUUID() ) );

		// Collect the member statistics
		StatCollector<User> collector = new StatCollector<>();
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

			collector.add( PILOT_FLIGHT_COUNT, user, flightServices.getPilotFlightCount( user ) );
			collector.add( PILOT_FLIGHT_TIME, user, flightServices.getPilotFlightTime( user ) );
			collector.add( OBSERVER_FLIGHT_COUNT, user, flightServices.getObserverFlightCount( user ) );
			collector.add( OBSERVER_FLIGHT_TIME, user, flightServices.getObserverFlightTime( user ) );
			flightServices.getFlightWithLongestTime( user ).ifPresent( f -> collector.add( PILOT_LONGEST_FLIGHT, user, f.duration() ) );
			collector.add( PILOT_FLIGHT_DATE, user, stats.getLastFlightTimestamp() );
			return stats;
		} ).toList();

		// Pilot records
		StatSeries<User> pilotFlightCounts = collector.get( PILOT_FLIGHT_COUNT );
		StatSeries<User> pilotFlightTimes = collector.get( PILOT_FLIGHT_TIME );
		StatSeries<User> pilotLongestFlight  = collector.get( PILOT_LONGEST_FLIGHT );
		StatSeries<User> pilotFlightDates = collector.get( PILOT_FLIGHT_DATE );

		// Aircraft records
		// NEXT Group aircraft record book (leader board?)
		// Aircraft with highest total flight count
		// Aircraft with highest total flight time
		// Aircraft with longest single flight time [this is a historical event] (what pilot, what date, what location)
		// Aircraft that flew most recently

		// Create a map for the dashboard
		Map<String, Object> map = new HashMap<>();
		map.put( DISPLAY_NAME, group.name() );
		map.put( PILOT_FLIGHT_COUNT, String.valueOf( (int)collector.get( PILOT_FLIGHT_COUNT ).getSum() ) );
		map.put( PILOT_FLIGHT_TIME, String.valueOf( (long)collector.get( PILOT_FLIGHT_TIME ).getSum() ) );
		map.put( OBSERVER_FLIGHT_COUNT, String.valueOf( (int)collector.get( OBSERVER_FLIGHT_COUNT ).getSum() ) );
		map.put( OBSERVER_FLIGHT_TIME, String.valueOf( (long)collector.get( OBSERVER_FLIGHT_TIME ).getSum() ) );
		if( memberStats.size() > 0 ) map.put( MEMBER_STATS, memberStats );

		addRecord( map, PilotRecord.TOTAL_FLIGHT_COUNT, Integer.toString( (int)pilotFlightCounts.getMax() ), "count", pilotFlightCounts.getMaxValueOwner().name() );
		addRecord( map, PilotRecord.TOTAL_FLIGHT_TIME, Integer.toString( (int)pilotFlightTimes.getMax() ), "duration", pilotFlightTimes.getMaxValueOwner().name() );
		if( pilotLongestFlight != null ) addRecord( map, PilotRecord.LONGEST_SINGLE_FLIGHT_TIME, Long.toString( (long)pilotLongestFlight.getMax() ), "duration", pilotLongestFlight.getMaxValueOwner().name() );
		addRecord( map, PilotRecord.MOST_RECENT_FLIGHT, Long.toString( (long)pilotFlightDates.getMax() ), "timestamp", pilotFlightDates.getMaxValueOwner().name() );

		return new AsyncResult<>( statePersisting.upsertProjection( group.dashboardId(), Json.stringify( map ) ) );
	}

	private void addRecord( Map<String, Object> map, PilotRecord record, String value, String type, String owner ) {
		map.put( record.getKey(), new Record( record.getName(), record.getDescription(), value, type, owner ) );
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

		private String status;

		private String baseColor;

		private String trimColor;

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
