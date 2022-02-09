package com.desertskyrangers.flightdeck.adapter.state;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.state.entity.GroupEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.PreferencesEntity;
import com.desertskyrangers.flightdeck.adapter.state.repo.GroupRepo;
import com.desertskyrangers.flightdeck.adapter.state.repo.PreferencesRepo;
import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.GroupType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StatePersistingServiceTest extends BaseTest {

	@Autowired
	private GroupRepo groupRepo;

	@Autowired
	private PreferencesRepo preferencesRepo;

	@Test
	void testUpsertGroup() {
		// when
		Group expected = statePersisting.upsert( createTestGroup( "Test Club", GroupType.CLUB ) );

		// then
		GroupEntity actual = groupRepo.findById( expected.id() ).orElse( null );
		assertThat( actual ).isNotNull();
		assertThat( GroupEntity.toGroup( actual ) ).isEqualTo( expected );
	}

	@Test
	void testRemoveGroup() {
		// when
		Group expected = statePersisting.upsert( createTestGroup( "Test Club", GroupType.CLUB ) );

		// then
		GroupEntity actual = groupRepo.findById( expected.id() ).orElse( null );
		assertThat( actual ).isNotNull();
		assertThat( GroupEntity.toGroup( actual ) ).isEqualTo( expected );

		// when
		statePersisting.remove( expected );

		// then
		assertThat( groupRepo.findById( expected.id() ).orElse( null ) ).isNull();
	}

	@Test
	void testUpsertPreferences() {
		// given
		var user = createTestUser();

		// when
		Map<String, Object> expected = statePersisting.upsertPreferences( user, Map.of() );

		// then
		PreferencesEntity actual = preferencesRepo.findById( user.id() ).orElse( null );
		assertThat( actual ).isNotNull();
		assertThat( PreferencesEntity.toPreferences( actual ) ).isEqualTo( expected );
	}

	@Test
	void testRemovePreferences() {
		// given
		var user = createTestUser();

		// when
		Map<String, Object> expected = statePersisting.upsertPreferences( user, Map.of() );

		// then
		PreferencesEntity actual = preferencesRepo.findById( user.id() ).orElse( null );
		assertThat( actual ).isNotNull();
		assertThat( PreferencesEntity.toPreferences( actual ) ).isEqualTo( expected );

		// when
		statePersisting.removePreferences( user );
		assertThat( preferencesRepo.findById( user.id() ).orElse( null ) ).isNull();
	}

}
