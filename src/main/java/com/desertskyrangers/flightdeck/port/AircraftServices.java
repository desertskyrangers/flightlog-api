package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Aircraft;
import com.desertskyrangers.flightdeck.core.model.AircraftStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AircraftServices {

	Optional<Aircraft> find( UUID id );

	List<Aircraft> findByOwner( UUID owner );

	List<Aircraft> findByOwnerAndStatus( UUID owner, AircraftStatus status );

	Aircraft upsert( Aircraft aircraft );

	void remove( Aircraft aircraft );

	Aircraft updateFlightData( Aircraft aircraft );

}
