package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.port.ProjectionServices;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectionService implements ProjectionServices {

	private final StateRetrieving stateRetrieving;

	@Autowired
	public ProjectionService( StateRetrieving stateRetrieving){
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public Optional<String> findProjection( UUID id ) {
		return stateRetrieving.findProjection( id );
	}

}
