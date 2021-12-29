package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthRequestingServiceTest {

	//	@Mock
	//	private StatePersisting statePersisting;

	@Mock
	private StateRetrieving stateRetrieving;

	//	@Mock
	//	private HumanInterface humanInterface;

	@InjectMocks
	private AuthRequestingService service;

	private Verification stored;

	@BeforeEach
	void setup() {
		UUID id = UUID.randomUUID();
		String code = "000000";
		long timestamp = System.currentTimeMillis();
		String type = Verification.EMAIL_VERIFY_TYPE;

		stored = new Verification();
		stored.id( id );
		stored.code( code );
		stored.timestamp( timestamp );
		stored.type( type );

		when( stateRetrieving.findVerification( id ) ).thenReturn( Optional.of( stored ) );
	}

	@Test
	void testRequestUserVerifySuccess() {
		// given
		Verification verification = new Verification();
		verification.id( stored.id() );
		verification.code( stored.code() );
		verification.timestamp( stored.timestamp() );
		verification.type( stored.type() );

		// when
		List<String> messages = service.requestUserVerify( verification );

		// then
		assertThat( messages.size() ).isEqualTo( 0 );
	}

	@Test
	void testRequestUserVerifyInvalidVerification() {
		// given
		Verification verification = new Verification();
		verification.id( UUID.randomUUID() );
		verification.code( stored.code() );
		verification.timestamp( stored.timestamp() );
		verification.type( stored.type() );

		// when
		List<String> messages = service.requestUserVerify( verification );

		// then
		assertThat( messages.get( 0 ) ).isEqualTo( "Verification code expired" );
	}

	@Test
	void testRequestUserVerifyInvalidVerificationCode() {
		// given
		Verification verification = new Verification();
		verification.id( stored.id() );
		verification.code( "123456" );
		verification.timestamp( stored.timestamp() );
		verification.type( stored.type() );

		// when
		List<String> messages = service.requestUserVerify( verification );

		// then
		assertThat( messages.get( 0 ) ).isEqualTo( "Invalid verification code: 123456" );
	}

	@Test
	void testRequestUserVerifyVerificationCodeExpired() {
		// given
		Verification verification = new Verification();
		verification.id( stored.id() );
		verification.code( stored.code() );
		verification.timestamp( stored.timestamp() + Verification.CODE_TIMEOUT + 1 );
		verification.type( stored.type() );

		// when
		List<String> messages = service.requestUserVerify( verification );

		// then
		assertThat( messages.get( 0 ) ).isEqualTo( "Verification code expired" );
	}

	@Test
	void testRequestUserVerifyVerificationTimestampInvalid() {
		// given
		Verification verification = new Verification();
		verification.id( stored.id() );
		verification.code( stored.code() );
		verification.timestamp( stored.timestamp() - 1 );
		verification.type( stored.type() );

		// when
		List<String> messages = service.requestUserVerify( verification );

		// then
		assertThat( messages.get( 0 ) ).isEqualTo( "Invalid verification timestamp" );
	}

}
