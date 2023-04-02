package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.AwardServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AwardServiceTest extends BaseTest {

	@Autowired
	AwardServices awardServices;

	@Autowired
	StatePersisting statePersisting;

	@Autowired
	StateRetrieving stateRetrieving;

	@Test
	void testCheckForAwards() {
		User user = statePersisting.upsert( createTestUser() );
		Flight flight = statePersisting.upsert( createTestFlight( user ) );

		assertThat( stateRetrieving.findAllFlights().size() ).isEqualTo( 1 );
		assertThat( awardServices.getAwards( user.id() ).getTotalElements() ).isEqualTo( 1 );
	}

}
