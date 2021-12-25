package com.desertskyrangers.flightlog.plug.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest( webEnvironment = RANDOM_PORT )
public class BasicConfigurationIntegrationTest {

	private TestRestTemplate restTemplate;

	private URL base;

	@LocalServerPort
	int port;

	@BeforeEach
	public void setUp() throws MalformedURLException {
		restTemplate = new TestRestTemplate( "user", "password" );
		base = new URL( "http://localhost:" + port + ApiPath.MONITOR_STATUS );
	}

	@Test
	public void whenLoggedUserRequestsHomePage_ThenSuccess() {
		ResponseEntity<String> response = restTemplate.getForEntity( base.toString(), String.class );

		assertThat( response.getStatusCode() ).isEqualTo( HttpStatus.OK );
		assertThat( response.getBody() ).contains( "version" );
	}

	@Test
	public void whenUserWithWrongCredentials_thenUnauthorizedPage() {
		restTemplate = new TestRestTemplate( "user", "wrongpassword" );
		ResponseEntity<String> response = restTemplate.getForEntity( base.toString(), String.class );

		assertThat( response.getStatusCode() ).isEqualTo( HttpStatus.UNAUTHORIZED );
		assertThat( response.getBody() ).isNull();
	}

}
