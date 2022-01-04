package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.jwt.JwtToken;
import com.desertskyrangers.flightlog.adapter.api.jwt.JwtTokenProvider;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@WithMockUser
@SpringBootTest( webEnvironment = RANDOM_PORT )
public class WebSecurityConfigurationTest {

	private TestRestTemplate restTemplate;

	private URL base;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@LocalServerPort
	int port;

	@BeforeEach
	public void setUp() throws MalformedURLException {
		restTemplate = new TestRestTemplate( "user", "password" );
		base = new URL( "http://localhost:" + port + ApiPath.PROFILE );

	}

	@Test
	public void whenLoggedUserRequestsHomePage_ThenSuccess() {
		// given
		UserAccount account = new UserAccount();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String jwtToken = tokenProvider.createToken( account, authentication, false );
		HttpHeaders headers = new HttpHeaders();
		headers.add( JwtToken.AUTHORIZATION_HEADER, JwtToken.AUTHORIZATION_TYPE + " " + jwtToken );
		HttpEntity<String> entity = new HttpEntity<>( "parameters", headers );

		// when
		ResponseEntity<String> response = restTemplate.exchange( base.toString(), HttpMethod.GET, entity, String.class );

		// then
		String username = ((User)authentication.getPrincipal()).getUsername();
		assertThat( response.getStatusCode() ).isEqualTo( HttpStatus.OK );
		assertThat( response.getBody() ).isEqualTo( "{\"username\":\"" + username + "\"}" );
	}

	@Test
	@WithMockUser
	public void whenUserWithWrongCredentials_ThenForbidden() {
		HttpEntity<String> entity = new HttpEntity<>( "parameters", new HttpHeaders() );

		ResponseEntity<String> response = restTemplate.exchange( base.toString(), HttpMethod.GET, entity, String.class );

		assertThat( response.getStatusCode() ).isEqualTo( HttpStatus.FORBIDDEN );
		assertThat( response.getBody() ).isNull();
	}

}
