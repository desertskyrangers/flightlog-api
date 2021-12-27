package com.desertskyrangers.flightlog.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextTest {

	@Test
	void testLpad() {
		assertThat( Text.lpad( "frank", 7 ) ).isEqualTo( "  frank" );
		assertThat( Text.lpad( "421", 6, '0' ) ).isEqualTo( "000421" );
	}

	@Test
	void testRpad() {
		assertThat( Text.rpad( "frank", 7 ) ).isEqualTo( "frank  " );
		assertThat( Text.rpad( "421", 6, '0' ) ).isEqualTo( "421000" );
	}

}
