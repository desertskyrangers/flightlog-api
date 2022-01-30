package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Group;

import java.util.Set;
import java.util.UUID;

public interface GroupService {

	void upsert( Group group );

	void remove( Group group );

	Set<Group> findGroupsByUser( UUID id );

}
