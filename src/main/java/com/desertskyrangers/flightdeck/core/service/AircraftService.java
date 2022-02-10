package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Aircraft;
import com.desertskyrangers.flightdeck.port.AircraftServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AircraftService implements AircraftServices {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public AircraftService( StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	public Optional<Aircraft> find( UUID id ) {
		return stateRetrieving.findAircraft( id );
	}

	public List<Aircraft> findByOwner( UUID owner ) {
		return stateRetrieving.findAircraftByOwner( owner );
	}

	public void upsert( Aircraft aircraft ) {
		statePersisting.upsert( aircraft );
	}

	public void remove( Aircraft aircraft ) {
		statePersisting.remove( aircraft );
	}

}
