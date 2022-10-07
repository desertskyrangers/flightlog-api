package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class AircraftService implements AircraftServices {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private final FlightServices flightServices;

	private final UserServices userServices;

	private DashboardServices dashboardServices;

	public AircraftService( StatePersisting statePersisting, StateRetrieving stateRetrieving, FlightServices flightServices, UserServices userServices ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
		this.flightServices = flightServices;
		this.userServices = userServices;

		this.flightServices.setAircraftServices( this );
	}

	public void setDashboardServices( DashboardServices dashboardServices ) {
		this.dashboardServices = dashboardServices;
	}

	public Optional<Aircraft> find( UUID id ) {
		return stateRetrieving.findAircraft( id );
	}

	public List<Aircraft> findAllByOwner( UUID owner ) {
		return stateRetrieving.findAircraftByOwner( owner );
	}

	public Page<Aircraft> findPageByOwner( UUID owner, int page, int size ) {
		return stateRetrieving.findAircraftPageByOwner( owner, page, size );
	}

	public Page<Aircraft> findPageByOwnerAndStatus( UUID owner, Set<AircraftStatus> status, int page, int size ) {
		return stateRetrieving.findAircraftPageByOwnerAndStatus( owner, status, page, size );
	}

	public List<Aircraft> findAllByOwnerAndStatus( UUID owner, AircraftStatus status ) {
		return stateRetrieving.findAircraftByOwnerAndStatus( owner, status );
	}

	public Aircraft upsert( Aircraft aircraft ) {
		int flightCount = flightServices.getAircraftFlightCount( aircraft );
		long flightTime = flightServices.getAircraftFlightTime( aircraft );

		aircraft.flightCount( flightCount );
		aircraft.flightTime( flightTime );

		Aircraft result = statePersisting.upsert( aircraft );
		userServices.find( aircraft.owner() ).ifPresent( dashboardServices::update );

		return result;
	}

	public void remove( Aircraft aircraft ) {
		statePersisting.remove( aircraft );
		userServices.find(aircraft.owner() ).ifPresent( dashboardServices::update );
	}

	public Aircraft updateFlightData( Aircraft aircraft ) {
		Optional<Aircraft> optional = stateRetrieving.findAircraft( aircraft.id() );
		optional.ifPresent( this::upsert );
		return aircraft;
	}

}
