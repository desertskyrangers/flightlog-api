package com.desertskyrangers.flightdeck.port;

import java.util.Optional;
import java.util.UUID;

public interface ProjectionServices {

	Optional<String> findProjection( UUID id );

}
