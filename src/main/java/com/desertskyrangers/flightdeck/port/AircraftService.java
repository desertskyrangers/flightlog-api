package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Aircraft;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AircraftService {

	Optional<Aircraft> find( UUID id );

	List<Aircraft> findByOwner( UUID owner );

	void upsert( Aircraft aircraft );

	void remove( Aircraft aircraft );

}
