package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Getter
public class CommonDashboardService<T extends Dashboard> {

	private final FlightServices flightServices;

	private final AircraftServices aircraftServices;

	private final UserServices userServices;

	private final StateRetrieving stateRetrieving;

	private final StatePersisting statePersisting;

	public CommonDashboardService( AircraftServices aircraftServices, FlightServices flightServices, UserServices userServices, StateRetrieving stateRetrieving, StatePersisting statePersisting ) {
		this.aircraftServices = aircraftServices;
		this.flightServices = flightServices;
		this.userServices = userServices;
		this.stateRetrieving = stateRetrieving;
		this.statePersisting = statePersisting;
	}

	protected T populate( User user, T dashboard ) {
		Map<String, Object> preferences = getUserServices().getPreferences( user );

		List<Aircraft> aircraft;
		// FIXME What if the dashboard is public???
		if( "true".equals( String.valueOf( preferences.get( PreferenceKey.SHOW_ALL_AIRCRAFT ) ) ) ) {
			aircraft = getAircraftServices().findByOwner( user.id() );
		} else {
			aircraft = getAircraftServices().findByOwnerAndStatus( user.id(), AircraftStatus.AIRWORTHY );
		}

		List<AircraftStats> aircraftStats = aircraft.stream().map( a -> {
			AircraftStats stats = new AircraftStats();
			stats.id( a.id() );
			stats.name( a.name() );
			stats.type( a.type() );
			stats.lastFlightTimestamp( getFlightServices().getLastAircraftFlight( a ).map( Flight::timestamp ).orElse( -1L ) );
			stats.flightCount( getFlightServices().getAircraftFlightCount( a ) );
			stats.flightTime( getFlightServices().getAircraftFlightTime( a ) );
			return stats;
		} ).toList();

		dashboard.flightCount( getFlightServices().getPilotFlightCount( user.id() ) );
		dashboard.flightTime( getFlightServices().getPilotFlightTime( user.id() ) );
		dashboard.observerCount( getFlightServices().getObserverFlightCount( user.id() ) );
		dashboard.observerTime( getFlightServices().getObserverFlightTime( user.id() ) );
		dashboard.lastPilotFlightTimestamp( getFlightServices().getLastPilotFlight( user ).map( Flight::timestamp ).orElse( -1L ) );
		if( aircraftStats.size() > 0 ) dashboard.aircraftStats( aircraftStats );

		return dashboard;
	}

}
