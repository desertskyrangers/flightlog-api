package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.User;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface GroupServices {

	Set<Group> findAllAvailable( User user );

	Optional<Group> find( UUID id );

	Group create( User requester, User owner, Group group );

	Group upsert( Group group );

	Group remove( Group group );

	Set<Group> findGroupsByUser( User user);

	Page<Group> findGroupsPageByUser( User user, int page, int size );

}
