package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import com.desertskyrangers.flightdeck.util.Names;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Deprecated
public class PublicDashboardService extends CommonDashboardService<PublicDashboard> implements PublicDashboardServices {

	public PublicDashboardService( AircraftServices aircraftServices, FlightServices flightServices, UserServices userServices, StateRetrieving stateRetrieving, StatePersisting statePersisting ) {
		super( aircraftServices, flightServices, userServices, stateRetrieving, statePersisting );
		flightServices.setPublicDashboardServices( this );
		userServices.setPublicDashboardServices( this );
	}

	@Override
	public Optional<PublicDashboard> find( UUID uuid ) {
		return Optional.empty();
	}

	@Override
	public Optional<PublicDashboard> findByUser( User user ) {
		update( user );
		return getStateRetrieving().findPublicDashboard( user );
	}

	@Override
	public PublicDashboard upsert( User user, PublicDashboard dashboard ) {
		return getStatePersisting().upsertPublicDashboard( user, dashboard );
	}

	@Override
	public PublicDashboard update( User user ) {
		PublicDashboard dashboard = populate( user, new PublicDashboard() );
		dashboard.displayName( Names.firstNameAndLastInitial( user ) );
		upsert( user, dashboard );
		return dashboard;
	}

	@Override
	public String update( Group group ) {
		return null;
	}

}
