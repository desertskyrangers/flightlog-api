package com.desertskyrangers.flightdeck;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppColorTest {

	@Test
	void testHashcode() {
		assertThat( AppColor.of( 32, 64, 128, 160 ).hashCode() ).isEqualTo( Integer.parseUnsignedInt( "a0204080", 16 ) );
		assertThat( AppColor.of( 32, 64, 128, 160 ).hashCode() ).isEqualTo( AppColor.of( 32, 64, 128, 160 ).getValue() );
	}

	@Test
	void testFromHex() {
		assertThat( AppColor.fromHex( "a0204080" ) ).isEqualTo( AppColor.of( 32, 64, 128, 160 ) );
		assertThat( AppColor.fromHex( "#a0204080" ) ).isEqualTo( AppColor.of( 32, 64, 128, 160 ) );
		assertThat( AppColor.fromHex( "0xa0204080" ) ).isEqualTo( AppColor.of( 32, 64, 128, 160 ) );
	}

	@Test
	void testToHex() {
		assertThat( AppColor.toHex( AppColor.of( 32, 64, 128, 160 ) ) ).isEqualTo( "0xa0204080" );
	}

	@Test
	void testFromWeb() {
		assertThat( AppColor.fromWeb( "204080a0" ) ).isEqualTo( AppColor.of( 32, 64, 128, 160 ) );
		assertThat( AppColor.fromWeb( "#204080a0" ) ).isEqualTo( AppColor.of( 32, 64, 128, 160 ) );
		assertThat( AppColor.fromWeb( "0x204080a0" ) ).isEqualTo( AppColor.of( 32, 64, 128, 160 ) );
	}

	@Test
	void testToWeb() {
		assertThat( AppColor.toWeb( AppColor.of( 32, 64, 128, 160 ) ) ).isEqualTo( "#204080a0" );
	}

}
