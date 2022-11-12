package com.desertskyrangers.flightdeck.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTest {

	private final Location flyingSpot = new Location().latitude(40.503336827514715).longitude(-112.0146147755627);

	private final Location morningCloakPark = new Location().latitude( 40.50353298117737 ).longitude( -112.01466589278837 ).name( "Morning Cloak Park" );

	private final Location monarchMeadowsPark = new Location().latitude( 40.50381857223479 ).longitude( -112.00869407480226 ).name( "Monarch Meadows Park" );

	@Test
	void testMetersToDegreesOnEarth() {
		assertThat( Location.metersToDegreesOnEarth( 1 ) ).isEqualTo( 0.000008983204953368848 );
	}

	@Test
	void testContainsWithLocation() {
		Location location = morningCloakPark.size( 300 );
		assertTrue( location.contains( flyingSpot ) );
		assertFalse( location.contains( monarchMeadowsPark.latitude(), monarchMeadowsPark.longitude() ) );
	}

	@Test
	void testContainsWithLatitudeAndLongitude() {
		Location location = morningCloakPark.size( 300 );
		assertTrue( location.containsByDiameter( flyingSpot.latitude(), flyingSpot.longitude() ) );
		assertFalse( location.containsByDiameter( monarchMeadowsPark.latitude(), monarchMeadowsPark.longitude() ) );
	}

	@Test
	void testContainsByDiameter() {
		Location location = morningCloakPark.size( 300 );
		assertTrue( location.containsByDiameter( flyingSpot.latitude(), flyingSpot.longitude() ) );
		assertFalse( location.containsByDiameter( monarchMeadowsPark.latitude(), monarchMeadowsPark.longitude() ) );
	}

	@Test
	void testContainsByRadius() {
		Location location = morningCloakPark.size( 150 );
		assertTrue( location.containsByRadius( flyingSpot.latitude(), flyingSpot.longitude() ) );
		assertFalse( location.containsByRadius( monarchMeadowsPark.latitude(), monarchMeadowsPark.longitude() ) );
	}

	@Test
	void testContainsBySquare() {
		Location location = morningCloakPark.size( 150 );
		assertTrue( location.containsBySquare( flyingSpot.latitude(), flyingSpot.longitude() ) );
		assertFalse( location.containsBySquare( monarchMeadowsPark.latitude(), monarchMeadowsPark.longitude() ) );
	}

}
