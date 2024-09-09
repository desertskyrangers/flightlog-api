package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.core.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LocationServiceTest extends BaseTest {

	@Test
	void testUpsert() {
		// given
		User user = statePersisting.upsert( createTestUser() );
		Location expected = createTestLocation( user );
		expected.altitude( 1973 );

		// when
		Location result = locationServices.upsert( expected );

		// then
		assertThat( result.altitude() ).isEqualTo( 1973 );
		assertThat( result.id() ).isEqualTo( expected.id() );
		assertThat( result ).isEqualTo( expected );
	}

	@Test
	void testRemove() {
		User user = statePersisting.upsert( createTestUser() );
		Location location = createTestLocation( user );
		locationServices.upsert( location );
		assertThat( locationServices.find( location.id() ).orElse( null ) ).isEqualTo( location );
		locationServices.remove( location );
		assertNull( locationServices.find( location.id() ).orElse( null ) );
	}

}
