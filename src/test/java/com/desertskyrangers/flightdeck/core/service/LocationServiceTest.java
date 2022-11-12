package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.LocationServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LocationServiceTest extends BaseTest {

	@Autowired
	private LocationServices userServices;

	@Test
	void testUpsert() {
		User user = statePersisting.upsert( createTestUser() );
		Location location = createTestLocation( user );
		userServices.upsert( location );
		assertThat( userServices.find( location.id() ).orElse( null ) ).isEqualTo( location );
	}

	@Test
	void testRemove() {
		User user = statePersisting.upsert( createTestUser() );
		Location location = createTestLocation( user );
		userServices.upsert( location );
		assertThat( userServices.find( location.id() ).orElse( null ) ).isEqualTo( location );
		userServices.remove( location );
		assertNull( userServices.find( location.id() ).orElse( null ) );
	}

}
