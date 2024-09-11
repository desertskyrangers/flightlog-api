package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactFlight;
import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.FlightUpsertRequest;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.FlightServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class FlightServiceTest extends BaseTest {

	@Autowired
	FlightServices flightServices;

	@Test
	void testUpdateWithCustomLocation() {
		// given
		User pilot = statePersisting.upsert( createTestUser() );
		Flight flight = statePersisting.upsert( createTestFlight( pilot ) );
		ReactFlight update = ReactFlight.from( pilot, flight );
		update.setLocation( Location.CUSTOM_LOCATION_ID.toString() );
		update.setLatitude( 40.50339561398912 );
		update.setLongitude( -112.01445893299925 );
		update.setAltitude( 1478 );
		FlightUpsertRequest request = ReactFlight.toUpsertRequest( update );

		// when
		Flight result = flightServices.upsert( request );

		// then
		assertThat( result.location().id() ).isEqualTo( Location.CUSTOM_LOCATION_ID );
		assertThat( result.latitude() ).isEqualTo( 40.50339561398912 );
		assertThat( result.longitude() ).isEqualTo( -112.01445893299925 );
		assertThat( result.altitude() ).isEqualTo( 1478 );
	}

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
