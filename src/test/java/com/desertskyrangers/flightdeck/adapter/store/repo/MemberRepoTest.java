package com.desertskyrangers.flightdeck.adapter.store.repo;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.store.entity.GroupEntity;
import com.desertskyrangers.flightdeck.adapter.store.entity.MemberEntity;
import com.desertskyrangers.flightdeck.adapter.store.entity.UserEntity;
import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberRepoTest extends BaseTest {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private GroupRepo groupRepo;

	@Autowired
	private MemberRepo memberRepo;

	@Test
	void testCreateAndRetrieve() {
		// given
		UserEntity user = userRepo.save( createTestUserEntity( "antonio", "antonio@example.com" ) );
		GroupEntity group = groupRepo.save( createTestGroupEntity( "Test Group", Group.Type.CLUB ) );
		MemberEntity member = memberRepo.save( createTestMemberEntity( user, group, Member.Status.ACCEPTED ) );

		// when
		MemberEntity actual = memberRepo.findById( member.getId() ).orElse( null );

		// then
		assertThat( actual ).isEqualTo( member );
	}

}
