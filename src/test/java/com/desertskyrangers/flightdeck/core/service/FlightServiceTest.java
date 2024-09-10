package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactFlight;
import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.FlightUpsertRequest;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.FlightServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class FlightServiceTest extends BaseTest {

	@Autowired
	FlightServices flightServices;

	@Test
	void testUpdateWithAltitude() {
		// given
		User pilot = statePersisting.upsert( createTestUser() );
		Flight flight = statePersisting.upsert( createTestFlight( pilot ) );
		ReactFlight update = ReactFlight.from( pilot, flight );
		update.setAltitude( 1973 );
		FlightUpsertRequest request = ReactFlight.toUpsertRequest( update );

		// when
		Flight result = flightServices.upsert( request );

		// then
		assertThat( result.altitude() ).isEqualTo( 1973 );
	}

}
