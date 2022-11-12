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

	Page<Location> findPageByUserAndStatus( User user, Set<LocationStatus> status, int page, int size );

}
