package com.desertskyrangers.flightdeck.adapter.state;

import com.desertskyrangers.flightdeck.adapter.state.entity.GroupEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightdeck.adapter.state.repo.GroupRepo;
import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.GroupType;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StatePersistingServiceTest {

	@Autowired
	private StatePersisting persisting;

	@Autowired
	private GroupRepo groupRepo;

	@Test
	void testUpsertGroup() {
		User owner = new User();
		persisting.upsert( owner );

		Group group = new Group();
		group.type( GroupType.CLUB );
		group.name( "Test Club" );
		group.owner( owner );
		group.members( Set.of( owner ) );
		persisting.upsert( group );

		GroupEntity actual = groupRepo.findById( group.id() ).orElse( null );
		assertThat( actual ).isNotNull();
		assertThat( actual.getId() ).isEqualTo( group.id() );
		assertThat( actual.getType() ).isEqualTo( group.type().name().toLowerCase() );
		assertThat( actual.getName() ).isEqualTo( group.name() );
		assertThat( actual.getOwner() ).isEqualTo( UserEntity.from( group.owner() ) );
		assertThat( actual.getMembers() ).containsAll( Set.of( UserEntity.from( owner ) ) );
	}

}
