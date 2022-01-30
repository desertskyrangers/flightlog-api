package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Group;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface GroupService {

	Optional<Group> find( UUID id );

	void upsert( Group group );

	void remove( Group group );

	Set<Group> findGroupsByUser( UUID id );

}
