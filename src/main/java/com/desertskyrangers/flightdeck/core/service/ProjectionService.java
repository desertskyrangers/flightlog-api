package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.port.ProjectionServices;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectionService implements ProjectionServices {

	@Override
	public Optional<String> findProjection( UUID id ) {
		// This is a JSON object
		return Optional.of( "{\"name\":\"TEST\",\"flightCount\":\"0\",\"flightTime\":\"0\"}" );
	}

}
