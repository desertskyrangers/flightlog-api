package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.state.entity.GroupEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.MemberEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepoTest extends BaseTest {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private GroupRepo groupRepo;

	@Autowired
	private MemberRepo memberRepo;

	@Test
	void testCreateAndRetrieve() {
		// given
		UserEntity user = userRepo.save( createTestUserEntity( "Test User", "testuser@example.com" ) );

		// when
		UserEntity actual = userRepo.findById( user.getId() ).orElse( null );

		// then
		assertThat( actual ).isEqualTo( user );
	}

	@Test
	void testUserGroups() {
		// given
		UserEntity user = userRepo.save( createTestUserEntity( "Test User", "testuser@example.com" ) );
		GroupEntity group = groupRepo.save( createTestGroupEntity( "Test Group", Group.Type.CLUB ) );
		memberRepo.save( createTestMemberEntity( user, group, Member.Status.ACCEPTED ) );

		// when
		UserEntity actual = userRepo.findById( user.getId() ).orElse( null );

		// then
		assertThat( actual ).isNotNull();
		assertThat( actual.getGroups() ).containsExactlyInAnyOrder( group );
	}

	@Test
	void testUserMembership() {
		// given
		UserEntity user = userRepo.save( createTestUserEntity( "Test User", "testuser@example.com" ) );
		GroupEntity group = groupRepo.save( createTestGroupEntity( "Test Group", Group.Type.CLUB ) );
		MemberEntity member = memberRepo.save( createTestMemberEntity( user, group, Member.Status.ACCEPTED ) );

		// when
		UserEntity actual = userRepo.findById( user.getId() ).orElse( null );

		// then
		assertThat( actual ).isNotNull();
		assertThat( actual.getMemberships() ).containsExactlyInAnyOrder( member );
	}

}
