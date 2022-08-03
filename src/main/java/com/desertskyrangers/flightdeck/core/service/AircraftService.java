package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.AircraftServices;
import com.desertskyrangers.flightdeck.port.FlightServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AircraftService implements AircraftServices {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private final FlightServices flightServices;

	public AircraftService( StatePersisting statePersisting, StateRetrieving stateRetrieving, FlightServices flightServices ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
		this.flightServices = flightServices;

		this.flightServices.setAircraftServices( this );
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

	public List<Aircraft> findAllByOwnerAndStatus( UUID owner, AircraftStatus status ) {
		return stateRetrieving.findAircraftByOwnerAndStatus( owner, status );
	}

	public Aircraft upsert( Aircraft aircraft ) {
		int flightCount = flightServices.getAircraftFlightCount( aircraft );
		long flightTime = flightServices.getAircraftFlightTime( aircraft );

		aircraft.flightCount( flightCount );
		aircraft.flightTime( flightTime );

		return statePersisting.upsert( aircraft );
	}

	public void remove( Aircraft aircraft ) {
		statePersisting.remove( aircraft );
	}

	public Aircraft updateFlightData( Aircraft aircraft ) {
		Optional<Aircraft> optional = stateRetrieving.findAircraft( aircraft.id() );
		optional.ifPresent( this::upsert );
		return aircraft;
	}

}
