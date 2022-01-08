package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.Aircraft;
import com.desertskyrangers.flightlog.port.StatePersisting;
import org.springframework.stereotype.Service;

@Service
public class AircraftService {

	private final StatePersisting statePersisting;

	public AircraftService( StatePersisting statePersisting ) {
		this.statePersisting = statePersisting;
	}

	public void upsert( Aircraft aircraft ) {
		statePersisting.upsert( aircraft );
	}

	public void remove( Aircraft aircraft ) {
		statePersisting.remove( aircraft );
	}

}
