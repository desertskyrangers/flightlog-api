package com.desertskyrangers.flightdeck.adapter.human;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HumanInterfaceServiceTest {

	@Test
	void testE164Format() {
		assertThat( HumanInterfaceService.e164Format( "8005550000" ) ).isEqualTo( "+18005550000" );
	}

	@Test
	void testE164FormatWithDashes() {
		assertThat( HumanInterfaceService.e164Format( "800-555-0000" ) ).isEqualTo( "+18005550000" );
	}

	@Test
	void testE164FormatWithParensAndDashes() {
		assertThat( HumanInterfaceService.e164Format( "(800)555-0000" ) ).isEqualTo( "+18005550000" );
	}

	@Test
	void testE164FormatWithLeadingOne() {
		assertThat( HumanInterfaceService.e164Format( "18005550000" ) ).isEqualTo( "+18005550000" );
	}

}
