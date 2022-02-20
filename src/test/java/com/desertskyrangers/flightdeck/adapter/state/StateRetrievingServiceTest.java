package com.desertskyrangers.flightdeck.adapter.state;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.state.entity.GroupEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightdeck.adapter.state.repo.GroupRepo;
import com.desertskyrangers.flightdeck.adapter.state.repo.UserRepo;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StateRetrievingServiceTest extends BaseTest {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private GroupRepo groupRepo;

	@Autowired
	private StateRetrieving stateRetrieving;

	@Test
	void testFindGroupById() {
		// given
		UserEntity owner = new UserEntity();
		owner.setId( UUID.randomUUID() );
		userRepo.save( owner );

		GroupEntity group = new GroupEntity();
		group.setId( UUID.randomUUID() );
		group.setType( GroupType.GROUP.name().toLowerCase() );
		group.setName( "Test Group" );
		//group.setOwner( owner );
		//group.setMembers( Set.of( owner ) );
		groupRepo.save( group );

		// when
		Group actual = stateRetrieving.findGroup( group.getId() ).orElse( null );

		// then
		assertThat( actual ).isNotNull();
		assertThat( actual.id() ).isEqualTo( group.getId() );
		assertThat( actual.type() ).isEqualTo( GroupType.valueOf( group.getType().toUpperCase() ) );
		//assertThat( actual.owner() ).isEqualTo( UserEntity.toUser( owner ) );
		//assertThat( actual.members() ).containsAll( Set.of( UserEntity.toUser( owner ) ) );
	}

	@Test
	void testFindGroupsByUserWithNoGroups() {
		// given
		User owner = statePersisting.upsert( createTestUser() );

		// when
		Set<Group> groups = stateRetrieving.findGroupsByOwner( owner );

		// then
		assertThat( groups ).isEmpty();
	}

	@Test
	void testFindGroupsByOwner() {
		// given
		User owner = statePersisting.upsert( createTestUser( "betty", "betty@example.com" ) );
		Group group = statePersisting.upsert( createTestGroup( "Test Group", GroupType.GROUP ) );
		statePersisting.upsert( new Member().user( owner ).group( group ).status( MemberStatus.OWNER ) );

		// when
		Set<Group> groups = stateRetrieving.findGroupsByOwner( owner );

		// then
		assertThat( groups ).containsAll( Set.of( group ) );
	}

	@Test
	void testFindAllAvailableGroups() {
		// given
		User jason = statePersisting.upsert( createTestUser( "jason", "jason@example.com" ) );
		Group jasons = statePersisting.upsert( createTestGroup( "Jason's Group", GroupType.GROUP ) );
		statePersisting.upsert( new Member().user( jason ).group( jasons ).status( MemberStatus.OWNER ) );
		User becky = statePersisting.upsert( createTestUser( "becky", "becky@example.com" ) );
		Group beckys = statePersisting.upsert( createTestGroup( "Becky's Group", GroupType.GROUP ) );
		statePersisting.upsert( new Member().user( becky ).group( beckys ).status( MemberStatus.OWNER ) );

		Set<Group> groups = stateRetrieving.findAllAvailableGroups( jason );

		// then
		assertThat( groups ).containsOnly( beckys );
	}

}
