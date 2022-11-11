package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Location;

import java.util.Optional;
import java.util.UUID;

public interface LocationServices {

	Optional<Location> find( UUID id );

	Location upsert( Location location );

	Location remove( Location location );

}
