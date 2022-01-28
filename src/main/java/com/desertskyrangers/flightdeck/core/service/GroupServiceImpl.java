package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.GroupService;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

	private final StateRetrieving stateRetrieving;

	public GroupServiceImpl( StateRetrieving stateRetrieving ) {
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public Set<Group> findGroupsByUser( UUID id ) {
		return stateRetrieving.findGroupsByUser( id );
	}

}
