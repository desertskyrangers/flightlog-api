package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Group;

import java.util.Set;
import java.util.UUID;

public interface GroupService {

	Set<Group> findGroupsByUser( UUID id );

}
