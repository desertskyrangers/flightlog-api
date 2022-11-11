package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.state.entity.LocationEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationRepoTest extends BaseTest {

	@Autowired
	private LocationRepo locationRepo;

	@Test
	void testCreateAndRetrieve() {
		LocationEntity location = locationRepo.save( createTestLocationEntity() );

		LocationEntity actual = locationRepo.findById( location.getId() ).orElse( null );

		assertThat( actual ).isEqualTo( location );
	}

}
