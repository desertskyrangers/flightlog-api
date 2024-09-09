package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactFlight;
import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.FlightUpsertRequest;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.FlightServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FlightServiceTest extends BaseTest {

	@Autowired
	FlightServices flightServices;

	@Test
	void testUpdateWithAltitude() {
		// given
		User pilot = createTestUser();
		statePersisting.upsert( pilot );
		Flight flight = createTestFlight( pilot );
		statePersisting.upsert( flight );
		ReactFlight update = ReactFlight.from( pilot, flight );
		FlightUpsertRequest request = ReactFlight.toUpsertRequest( update );
		request.altitude( 1973 );

		// when
		Flight result = flightServices.upsert( request );

		// then
		assertThat( result.altitude() ).isEqualTo( 1973 );
	}

}
