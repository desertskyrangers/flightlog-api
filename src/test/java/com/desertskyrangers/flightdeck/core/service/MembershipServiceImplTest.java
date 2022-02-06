package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.MembershipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MembershipServiceImplTest extends BaseTest {

	@Autowired
	MembershipService membershipService;

	@Test
	void testRequestMembership() {
		// given
		User owner = statePersisting.upsert( createTestUser( "olivia", "olivia@example.com" ) );
		User invitee = statePersisting.upsert( createTestUser( "nate", "nate@example.com" ) );
		statePersisting.upsert( createTestToken( invitee, invitee.username(), "" ) );
		Group group = statePersisting.upsert( createTestGroup( "Group A", GroupType.CLUB ) );
		statePersisting.upsert( createTestMember( owner, group, MemberStatus.OWNER ) );

		// when
		Member invitation = membershipService.requestMembership( owner, invitee, group, MemberStatus.INVITED );

		// then
		Set<Member> memberships = membershipService.findMembershipsByUser( invitee );
		assertThat( memberships ).containsExactlyInAnyOrder( invitation );
	}

}
