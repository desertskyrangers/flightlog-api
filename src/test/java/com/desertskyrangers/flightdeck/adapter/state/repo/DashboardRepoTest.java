package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.state.entity.DashboardProjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DashboardRepoTest extends BaseTest {

	@Autowired
	DashboardRepo dashboardRepo;

	@Test
	void testSaveAndFind() {
		DashboardProjection expected = new DashboardProjection().setId( UUID.randomUUID() ).setJson( "{}" );
		dashboardRepo.save( expected );

		DashboardProjection actual = dashboardRepo.findById( expected.getId() ).orElse( null );

		assertThat( actual ).isNotNull();
		assertThat( actual ).isEqualTo( expected );
	}

}
