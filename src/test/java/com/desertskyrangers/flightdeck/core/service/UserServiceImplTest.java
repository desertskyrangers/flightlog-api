package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.GroupType;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import com.desertskyrangers.flightdeck.port.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceImplTest {

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private UserService userService;

	@Test
	void testFindUsersInGroups() {
		// given
		User owner = new User();
		statePersisting.upsert( owner );

		Group group = new Group();
		group.id( UUID.randomUUID() );
		group.type( GroupType.GROUP );
		group.name( "Test Group" );
		group.owner( owner );
		group.members( Set.of( owner ) );
		statePersisting.upsert( group );

		// when
		Set<User> users = userService.findAllGroupPeers( owner );

		// then
		assertThat( users ).containsAll( Set.of( owner ) );
	}

}
