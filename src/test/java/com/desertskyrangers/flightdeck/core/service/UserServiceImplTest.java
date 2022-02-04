package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceImplTest extends BaseTest {

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private UserService userService;

	@Test
	void testFindAllGroupPeers() {
		// given
		User john = statePersisting.upsert( createTestUser( "john", "john@example.com" ) );
		User kara = statePersisting.upsert( createTestUser( "kara", "kara@example.com" ) );
		Group group = statePersisting.upsert( createTestGroup( "Test Group", GroupType.GROUP ) );
		statePersisting.upsert( new Member().user( john ).group( group ).status( MemberStatus.OWNER ) );
		statePersisting.upsert( new Member().user( kara ).group( group ).status( MemberStatus.OWNER ) );

		// when
		Set<User> users = userService.findAllGroupPeers( john );

		// then
		assertThat( users ).containsAll( Set.of( kara ) );
	}

}
