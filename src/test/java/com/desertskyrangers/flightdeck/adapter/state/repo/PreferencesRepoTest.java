package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.state.entity.PreferencesEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PreferencesRepoTest extends BaseTest {

	@Autowired
	PreferencesRepo preferencesRepo;

	@Test
	void testSaveAndFind() {
		PreferencesEntity expected = new PreferencesEntity().setId( UUID.randomUUID() ).setJson( "{}" );
		preferencesRepo.save( expected );

		PreferencesEntity actual = preferencesRepo.findById( expected.getId() ).orElse( null );

		assertThat( actual ).isEqualTo( expected );
	}

}
