package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.state.entity.FlightEntity;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

public class FlightRepoTest extends BaseTest {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private FlightRepo flightRepo;

	@Autowired
	private StatePersisting statePersisting;

	@Test
	void testFindWithPageable() {
		// given
		User pilot = statePersisting.upsert( createTestUser( "otto", "otto@exemple.com" ) );
		flightRepo.save( createTestFlightEntity( pilot ) );
		flightRepo.save( createTestFlightEntity( pilot ) );
		flightRepo.save( createTestFlightEntity( pilot ) );
		flightRepo.save( createTestFlightEntity( pilot ) );
		flightRepo.save( createTestFlightEntity( pilot ) );

		// when
		Page<FlightEntity> actual = flightRepo.findAll( PageRequest.of( 0, 3, Sort.Direction.DESC, "timestamp" ) );

		// then
		assertThat( actual ).isNotNull();
		assertThat( actual.getSize() ).isEqualTo( 3 );
		assertThat( actual.getTotalElements() ).isEqualTo( 5 );
	}

	private FlightEntity createTestFlightEntity( User pilot ) {
		return FlightEntity.from( createTestFlight( pilot ) );
	}

}
