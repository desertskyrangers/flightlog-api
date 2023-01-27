package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.UserServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest extends BaseTest {

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private UserServices userServices;

	@Test
	void testFindAllGroupPeers() {
		// given
		User john = statePersisting.upsert( createTestUser( "john", "john@example.com" ) );
		User kara = statePersisting.upsert( createTestUser( "kara", "kara@example.com" ) );
		User paul = statePersisting.upsert( createTestUser( "paul", "paul@example.com" ) );
		User sara = statePersisting.upsert( createTestUser( "sara", "sara@example.com" ) );
		Group group = statePersisting.upsert( createTestGroup( "Test Group", Group.Type.GROUP ) );
		statePersisting.upsert( new Member().user( john ).group( group ).status( Member.Status.OWNER ) );
		statePersisting.upsert( new Member().user( kara ).group( group ).status( Member.Status.REQUESTED ) );
		statePersisting.upsert( new Member().user( paul ).group( group ).status( Member.Status.REVOKED ) );
		statePersisting.upsert( new Member().user( sara ).group( group ).status( Member.Status.ACCEPTED ) );

		// when
		Set<User> users = userServices.findAllGroupPeers( john );

		// then
		assertThat( users ).containsExactlyInAnyOrder( kara, paul, sara );
	}

	@Test
	void testFindAllAcceptedGroupPeers() {
		// given
		User john = statePersisting.upsert( createTestUser( "john", "john@example.com" ) );
		User kara = statePersisting.upsert( createTestUser( "kara", "kara@example.com" ) );
		User paul = statePersisting.upsert( createTestUser( "paul", "paul@example.com" ) );
		User sara = statePersisting.upsert( createTestUser( "sara", "sara@example.com" ) );
		Group group = statePersisting.upsert( createTestGroup( "Test Group", Group.Type.GROUP ) );
		statePersisting.upsert( new Member().user( john ).group( group ).status( Member.Status.OWNER ) );
		statePersisting.upsert( new Member().user( kara ).group( group ).status( Member.Status.REQUESTED ) );
		statePersisting.upsert( new Member().user( paul ).group( group ).status( Member.Status.REVOKED ) );
		statePersisting.upsert( new Member().user( sara ).group( group ).status( Member.Status.ACCEPTED ) );

		// when
		Set<User> users = userServices.findAllAcceptedGroupPeers( john );

		// then
		assertThat( users ).containsExactlyInAnyOrder( sara );
	}

}
