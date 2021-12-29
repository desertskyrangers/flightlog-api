package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.StatePersisting;
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
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthRequestingServiceTest {

	@Mock
	private StatePersisting statePersisting;

	@Mock
	private StateRetrieving stateRetrieving;

	//	@Mock
	//	private HumanInterface humanInterface;

	@InjectMocks
	private AuthRequestingService service;

	private UserAccount account;

	private Verification stored;

	@BeforeEach
	void setup() {
		account = new UserAccount();

		UUID id = UUID.randomUUID();
		String code = "000000";
		long timestamp = System.currentTimeMillis();
		String type = Verification.EMAIL_VERIFY_TYPE;

		stored = new Verification();
		stored.id( id );
		stored.code( code );
		stored.timestamp( timestamp );
		stored.type( type );
		stored.userId( account.id() );

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
		verification.userId( account.id() );

		when( stateRetrieving.findUserAccount( account.id() ) ).thenReturn( Optional.of( account ) );

		// when
		List<String> messages = service.requestUserVerify( verification );

		// then
		verify( statePersisting, times( 1 ) ).upsert( account );
		verify( statePersisting, times( 1 ) ).remove( verification );
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
		verification.userId( account.id() );

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
		verification.userId( account.id() );

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
		verification.userId( account.id() );

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
		verification.userId( account.id() );

		// when
		List<String> messages = service.requestUserVerify( verification );

		// then
		assertThat( messages.get( 0 ) ).isEqualTo( "Invalid verification timestamp" );
	}

}
