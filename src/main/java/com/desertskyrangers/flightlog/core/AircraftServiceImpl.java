package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.Aircraft;
import com.desertskyrangers.flightlog.port.AircraftService;
import com.desertskyrangers.flightlog.port.StatePersisting;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AircraftServiceImpl implements AircraftService {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public AircraftServiceImpl( StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
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
