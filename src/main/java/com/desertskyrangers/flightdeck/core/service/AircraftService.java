package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.AircraftServices;
import com.desertskyrangers.flightdeck.port.FlightServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
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

	public List<Aircraft> findByOwner( UUID owner ) {
		return stateRetrieving.findAircraftByOwner( owner );
	}

	public List<Aircraft> findByOwnerAndStatus( UUID owner, AircraftStatus status ) {
		return stateRetrieving.findAircraftByOwnerAndStatus( owner, status );
	}

	public Aircraft upsert( Aircraft aircraft ) {
		return statePersisting.upsert( aircraft );
	}

	public void remove( Aircraft aircraft ) {
		statePersisting.remove( aircraft );
	}

	public Aircraft updateFlightData( Aircraft aircraft ) {
		Optional<Aircraft> optional = stateRetrieving.findAircraft( aircraft.id() );
		if( optional.isEmpty() ) return aircraft;

		int flightCount = flightServices.getAircraftFlightCount( aircraft );
		long flightTime = flightServices.getAircraftFlightTime( aircraft );

		log.warn( "Flight count=" + flightCount );

		aircraft = optional.get();
		aircraft.flightCount( flightCount );
		aircraft.flightTime( flightTime );

		return upsert( aircraft );
	}

}
