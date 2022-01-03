package com.desertskyrangers.flightlog.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailTest {

	@Test
	void testIsValid() {
		assertThat( Email.isValid( "a@b.cc" ) ).isTrue();
	}

	@Test
	void textIsValidWithBadPatterns() {
		assertThat( Email.isValid( null ) ).isFalse();
		assertThat( Email.isValid( "" ) ).isFalse();
		assertThat( Email.isValid( " " ) ).isFalse();
		assertThat( Email.isValid( "a" ) ).isFalse();
		assertThat( Email.isValid( "a@b" ) ).isFalse();
		assertThat( Email.isValid( "b.c" ) ).isFalse();
		assertThat( Email.isValid( "a@b.c" ) ).isFalse();
	}
}
