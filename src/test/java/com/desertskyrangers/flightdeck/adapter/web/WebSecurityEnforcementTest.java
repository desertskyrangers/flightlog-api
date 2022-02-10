package com.desertskyrangers.flightdeck.adapter.web;

import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtToken;
import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtTokenProvider;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactProfileResponse;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactUser;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest( webEnvironment = RANDOM_PORT )
public class WebSecurityEnforcementTest {

	private TestRestTemplate restTemplate;

	private URL url;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@LocalServerPort
	int port;

	@BeforeEach
	public void setUp() throws MalformedURLException {
		restTemplate = new TestRestTemplate( "user", "password" );
		url = new URL( "http://localhost:" + port + ApiPath.PROFILE );
	}

	@Test
	public void whenUserWithValidJwtRequestsProfilePage_ThenSuccess() {
		// given
		Authentication authentication = new TestingAuthenticationToken( "testuser", "password", "USER" );
		String jwtToken = tokenProvider.createToken( new User(), authentication, false );

		HttpHeaders headers = new HttpHeaders();
		headers.add( JwtToken.AUTHORIZATION_HEADER, JwtToken.AUTHORIZATION_TYPE + " " + jwtToken );
		HttpEntity<String> entity = new HttpEntity<>( "parameters", headers );

		// when
		ResponseEntity<String> response = restTemplate.exchange( url.toString(), HttpMethod.GET, entity, String.class );

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( new ReactUser() ) );
		assertThat( response.getStatusCode() ).isEqualTo( HttpStatus.BAD_REQUEST );
		assertThat( response.getBody() ).isEqualTo( accountJson );
	}

	@Test
	public void whenUserWithInvalidJwtRequestsProfilePage_ThenForbidden() {
		String jwtToken = "fake.jwt.token";
		HttpHeaders headers = new HttpHeaders();
		headers.add( JwtToken.AUTHORIZATION_HEADER, JwtToken.AUTHORIZATION_TYPE + " " + jwtToken );
		HttpEntity<String> entity = new HttpEntity<>( "parameters", headers );

		ResponseEntity<String> response = restTemplate.exchange( url.toString(), HttpMethod.GET, entity, String.class );

		assertThat( response.getStatusCode() ).isEqualTo( HttpStatus.FORBIDDEN );
		assertThat( response.getBody() ).isNull();
	}

}
