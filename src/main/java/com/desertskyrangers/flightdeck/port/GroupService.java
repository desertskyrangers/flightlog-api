package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Group;

import java.util.List;
import java.util.UUID;

public interface GroupService {
	List<Group> findGroupsByUser( UUID id );
}
