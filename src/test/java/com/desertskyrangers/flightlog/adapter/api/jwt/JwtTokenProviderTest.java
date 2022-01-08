package com.desertskyrangers.flightlog.adapter.api.jwt;

import com.desertskyrangers.flightlog.core.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTokenProviderTest {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Test
	void testCreateToken() {
		// given
		User account = new User();
		Authentication authentication = new TestingAuthenticationToken( "username", "password", "TESTER" );
		boolean remember = false;
		long timestamp = System.currentTimeMillis();
		int expiration = (int)(timestamp / 1000 + tokenProvider.getJwtValidityInSeconds());

		// when
		String token = tokenProvider.createToken( account, authentication, remember, timestamp );

		// then
		Map<String, Object> claims = tokenProvider.parse( token );
		assertThat( claims.get( JwtToken.USER_ID_CLAIM_KEY ) ).isEqualTo( account.id().toString() );
		assertThat( claims.get( JwtToken.SUBJECT_CLAIM_KEY ) ).isEqualTo( "username" );
		assertThat( claims.get( JwtToken.AUTHORITIES_CLAIM_KEY ) ).isEqualTo( "TESTER" );
		assertThat( claims.get( JwtToken.EXPIRES_CLAIM_KEY ) ).isEqualTo( expiration );
	}

	@Test
	void testCreateRememberedToken() {
		// given
		User account = new User();
		Authentication authentication = new TestingAuthenticationToken( "username", "password", "TESTER" );
		boolean remember = true;
		long timestamp = System.currentTimeMillis();
		int expiration = (int)(timestamp / 1000 + tokenProvider.getRememberedJwtValidityInSeconds());

		// when
		String token = tokenProvider.createToken( account, authentication, remember, timestamp );

		// then
		Map<String, Object> claims = tokenProvider.parse( token );
		assertThat( claims.get( JwtToken.USER_ID_CLAIM_KEY ) ).isEqualTo( account.id().toString() );
		assertThat( claims.get( JwtToken.SUBJECT_CLAIM_KEY ) ).isEqualTo( "username" );
		assertThat( claims.get( JwtToken.AUTHORITIES_CLAIM_KEY ) ).isEqualTo( "TESTER" );
		assertThat( claims.get( JwtToken.EXPIRES_CLAIM_KEY ) ).isEqualTo( expiration );
	}
}
