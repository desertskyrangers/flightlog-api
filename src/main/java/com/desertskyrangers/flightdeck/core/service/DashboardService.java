package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
	public Dashboard update( User user ) {
		Dashboard dashboard = populate( user, new Dashboard() );
		upsert( user, dashboard );
		return dashboard;
	}

}
