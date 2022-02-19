package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Dashboard;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.DashboardServices;
import com.desertskyrangers.flightdeck.port.FlightServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class DashboardService implements DashboardServices {

	private final FlightServices flightServices;

	private final StateRetrieving stateRetrieving;

	private final StatePersisting statePersisting;

	public DashboardService( FlightServices flightServices, StateRetrieving stateRetrieving, StatePersisting statePersisting ) {
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
		Dashboard dashboard = new Dashboard();
		dashboard.flightCount( flightServices.getPilotFlightCount( user.id() ) );
		dashboard.flightTime( flightServices.getPilotFlightTime( user.id() ) );
		dashboard.observerCount( flightServices.getObserverFlightCount( user.id() ) );
		dashboard.observerTime( flightServices.getObserverFlightTime( user.id() ) );
		upsert( user, dashboard );
		return dashboard;
	}

}
