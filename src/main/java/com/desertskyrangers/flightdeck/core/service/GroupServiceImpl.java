package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.port.GroupService;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public GroupServiceImpl( StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public void upsert( Group group ) {
		statePersisting.upsert( group );
	}

	@Override
	public void remove( Group group ) {
		statePersisting.remove( group );
	}

	@Override
	public Set<Group> findGroupsByUser( UUID id ) {
		return stateRetrieving.findGroupsByUser( id );
	}

}
