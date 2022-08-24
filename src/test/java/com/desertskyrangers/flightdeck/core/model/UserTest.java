package com.desertskyrangers.flightdeck.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

	@Test
	void testName() {
		User user = new User().firstName( "Uma" ).lastName( "Uvalde" );
		assertThat( user.name() ).isEqualTo( "Uma Uvalde" );
	}

	@Test
	void testNameWithPreferredName() {
		User user = new User().firstName( "Uma" ).lastName( "Uvalde" ).preferredName("Alice");
		assertThat( user.name() ).isEqualTo( "Alice" );
	}

	@Test
	void testNameWithBlankPreferredName() {
		User user = new User().firstName( "Uma" ).lastName( "Uvalde" ).preferredName("");
		assertThat( user.name() ).isEqualTo( "Uma Uvalde" );
	}

	@Test
	void testNameWithCallSign() {
		User user = new User().firstName( "Uma" ).lastName( "Uvalde" ).callSign("Unicorn");
		assertThat( user.name() ).isEqualTo( "Uma \"Unicorn\" Uvalde" );
	}

	@Test
	void testNameWithPreferredNameAndCallSign() {
		User user = new User().firstName( "Uma" ).lastName( "Uvalde" ).preferredName("Alice").callSign("Unicorn");
		assertThat( user.name() ).isEqualTo( "Alice" );
	}

}
