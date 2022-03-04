package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class DashboardService implements DashboardServices {

	private final FlightServices flightServices;

	private final AircraftServices aircraftServices;

	private final StateRetrieving stateRetrieving;

	private final StatePersisting statePersisting;

	public DashboardService( AircraftServices aircraftServices, FlightServices flightServices, StateRetrieving stateRetrieving, StatePersisting statePersisting ) {
		this.aircraftServices = aircraftServices;
		this.flightServices = flightServices;
		this.stateRetrieving = stateRetrieving;
		this.statePersisting = statePersisting;
	}

	@Override
	public Optional<Dashboard> find( UUID uuid ) {
		return Optional.empty();
	}

	@Override
	public Optional<Dashboard> findByUser( User user ) {
		// Temporarily update the dashboard any time it is requested
		update( user );

		return stateRetrieving.findDashboard( user );
	}

	@Override
	public Dashboard upsert( User user, Dashboard dashboard ) {
		return statePersisting.upsertDashboard( user, dashboard );
	}

	public Dashboard update( User user ) {
		List<AircraftStats> aircraftStats = aircraftServices.findByOwnerAndStatus( user.id(), AircraftStatus.AIRWORTHY ).stream().map( a -> {
			AircraftStats stats = new AircraftStats();
			stats.id( a.id() );
			stats.name( a.name() );
			stats.type( a.type() );
			stats.lastFlightTimestamp( flightServices.getLastAircraftFlight( a ).map( Flight::timestamp ).orElse( -1L ) );
			stats.flightCount( flightServices.getAircraftFlightCount( a ) );
			stats.flightTime( flightServices.getAircraftFlightTime( a ) );
			return stats;
		} ).toList();

		Dashboard dashboard = new Dashboard();
		dashboard.flightCount( flightServices.getPilotFlightCount( user.id() ) );
		dashboard.flightTime( flightServices.getPilotFlightTime( user.id() ) );
		dashboard.observerCount( flightServices.getObserverFlightCount( user.id() ) );
		dashboard.observerTime( flightServices.getObserverFlightTime( user.id() ) );
		dashboard.lastPilotFlightTimestamp( flightServices.getLastPilotFlight( user ).map( Flight::timestamp ).orElse( -1L ) );
		if( aircraftStats.size() > 0 ) dashboard.aircraftStats( aircraftStats );

		upsert( user, dashboard );
		return dashboard;
	}

}
