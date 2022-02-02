package com.desertskyrangers.flightdeck.adapter.state;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.state.entity.GroupEntity;
import com.desertskyrangers.flightdeck.adapter.state.repo.GroupRepo;
import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.GroupType;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StatePersistingServiceTest extends BaseTest {

	@Autowired
	private StatePersisting persisting;

	@Autowired
	private GroupRepo groupRepo;

	@Test
	void testUpsertGroup() {
		// given
		Group group = persisting.upsert( createTestGroup( "Test Club", GroupType.CLUB ) );

		// when
		GroupEntity actual = groupRepo.findById( group.id() ).orElse( null );

		// then
		assertThat( actual ).isNotNull();
		assertThat( GroupEntity.toGroup( actual ) ).isEqualTo( group );
	}

}
