package com.desertskyrangers.flightdeck.adapter.state;

import com.desertskyrangers.flightdeck.adapter.state.entity.GroupEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightdeck.adapter.state.repo.GroupRepo;
import com.desertskyrangers.flightdeck.adapter.state.repo.UserRepo;
import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.GroupType;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StateRetrievingServiceTest {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private GroupRepo groupRepo;

	@Autowired
	private StateRetrieving retrieving;

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
		group.setOwner( owner );
		group.setMembers( Set.of( owner ) );
		groupRepo.save( group );

		// when
		Group actual = retrieving.findGroup( group.getId() ).orElse( null );

		// then
		assertThat( actual ).isNotNull();
		assertThat( actual.id() ).isEqualTo( group.getId() );
		assertThat( actual.type() ).isEqualTo( GroupType.valueOf( group.getType().toUpperCase() ) );
		assertThat( actual.owner() ).isEqualTo( UserEntity.toUserAccount( owner ) );
		assertThat( actual.members() ).containsAll( Set.of( UserEntity.toUserAccount( owner ) ) );
	}

}
