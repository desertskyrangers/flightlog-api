package com.desertskyrangers.flightdeck.adapter.store.repo;

import com.desertskyrangers.flightdeck.util.AppColor;
import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.store.entity.AircraftEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AircraftRepoTest extends BaseTest {

	@Autowired
	AircraftRepo aircraftRepo;

	@Test
	void testSaveAndFind() {
		AircraftEntity expected = new AircraftEntity()
			.setId( UUID.randomUUID() )
			.setName( "TRINITY" )
			.setOwner( UUID.randomUUID() )
			.setOwnerType( "" )
			.setBaseColor( AppColor.of( 255,255,255))
			.setTrimColor( AppColor.fromWeb( "#204080a0" ) );
		aircraftRepo.save( expected );

		AircraftEntity actual = aircraftRepo.findById( expected.getId() ).orElse( null );

		assertThat( actual ).isNotNull();
		assertThat( actual ).isEqualTo( expected );
	}

}
