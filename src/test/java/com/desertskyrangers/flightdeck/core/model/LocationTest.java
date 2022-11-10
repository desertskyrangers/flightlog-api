package com.desertskyrangers.flightdeck.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTest {

	@Test
	void testMetersToDegreesOnEarth() {
		assertThat( Location.metersToDegreesOnEarth( 1 ) ).isEqualTo( 0.000008983204953368848 );
	}

	@Test
	void testContainsByRadius() {
		Location location = new Location().latitude( 40.50353298117737 ).longitude( -112.01466589278837 ).name( "Morning Cloak Park" ).radius( 150 );
		assertTrue( location.containsByRadius( 40.503336827514715, -112.0146147755627 ) );
		// Monarch Meadows Park
		assertFalse( location.containsByRadius( 40.50381857223479, -112.00869407480226 ) );
	}

	@Test
	void testContainsBySquare() {
		Location location = new Location().latitude( 40.50353298117737 ).longitude( -112.01466589278837 ).name( "Morning Cloak Park" ).radius( 150 );
		assertTrue( location.containsBySquare( 40.503336827514715, -112.0146147755627 ) );
		// Monarch Meadows Park
		assertFalse( location.containsBySquare( 40.50381857223479, -112.00869407480226 ) );
	}

}
