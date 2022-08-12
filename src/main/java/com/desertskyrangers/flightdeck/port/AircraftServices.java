package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Aircraft;
import com.desertskyrangers.flightdeck.core.model.AircraftStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AircraftServices {

	Optional<Aircraft> find( UUID id );

	List<Aircraft> findAllByOwner( UUID owner );

	List<Aircraft> findAllByOwnerAndStatus( UUID owner, AircraftStatus status );

	Page<Aircraft> findPageByOwner( UUID owner, int page, int size );

	Page<Aircraft> findPageByOwnerAndStatus( UUID owner, Set<AircraftStatus> status, int page, int size );

	Aircraft upsert( Aircraft aircraft );

	void remove( Aircraft aircraft );

	Aircraft updateFlightData( Aircraft aircraft );

}
