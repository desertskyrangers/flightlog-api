package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.*;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface LocationServices {

	Optional<Location> find( UUID id );

	Location upsert( Location location );

	Location remove( Location location );

	Set<Location> findByUser( User user );

	Set<Location> findByUserAndStatus( User user, Set<Location.Status> status );

	Page<Location> findPageByUserAndStatus( User user, Set<Location.Status> status, int page, int size );

}
