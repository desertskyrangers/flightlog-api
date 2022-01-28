package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.GroupEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightdeck.core.model.GroupType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GroupRepoTest {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private GroupRepo groupRepo;

	@Test
	void testCreateAndRetrieve() {
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

		GroupEntity actual = groupRepo.findById( group.getId() ).orElse( null );
		assertThat( actual ).isNotNull();
		assertThat( actual.getId() ).isEqualTo( group.getId() );
		assertThat( actual.getType() ).isEqualTo( group.getType() );
		assertThat( actual.getName() ).isEqualTo( group.getName() );
		assertThat( actual.getOwner() ).isEqualTo( group.getOwner() );
		assertThat( actual.getMembers() ).containsAll( Set.of( owner ) );
	}

}
