package com.desertskyrangers.flightdeck.adapter.api;

import com.desertskyrangers.flightdeck.adapter.api.jwt.JwtToken;
import com.desertskyrangers.flightdeck.adapter.api.jwt.JwtTokenProvider;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactProfileResponse;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactUserAccount;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.AircraftService;
import com.desertskyrangers.flightdeck.port.UserService;
import com.desertskyrangers.flightdeck.util.Json;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith( MockitoExtension.class )
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private AircraftService aircraftService;

	@Autowired
	private JwtTokenProvider tokenProvider;

	private User user;

	private HttpHeaders headers;

	@BeforeEach
	void setup() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if( authentication != null ) {
			String username = authentication.getName();
			String password = Objects.toString( authentication.getCredentials() );

			// Delete the exising mock user account
			userService.findByPrincipal( authentication.getName() ).ifPresent( u -> userService.remove( u ) );

			// Create mock user account
			user = new User();
			user.lastName( username );
			user.tokens( Set.of( new UserToken().principal( username ).credential( password ) ) );
			userService.upsert( user );

			String jwtToken = tokenProvider.createToken( user, authentication, false );

			headers = new HttpHeaders();
			headers.add( JwtToken.AUTHORIZATION_HEADER, JwtToken.AUTHORIZATION_TYPE + " " + jwtToken );
		}
	}

	@AfterEach
	void teardown() {
		userService.remove( user );
	}

	@Test
	void testGetProfile() throws Exception {
		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.PROFILE ).with( csrf() ).headers( headers ) ).andExpect( status().isOk() ).andReturn();

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( ReactUserAccount.from( user ) ) );
		assertThat( result.getResponse().getContentAsString() ).isEqualTo( accountJson );
	}

	@Test
	void testGetAccount() throws Exception {
		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER + "/" + user.id() ).with( csrf() ).headers( headers ) ).andExpect( status().isOk() ).andReturn();

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( ReactUserAccount.from( user ) ) );
		assertThat( result.getResponse().getContentAsString() ).isEqualTo( accountJson );
	}

	@Test
	void testUpdateAccount() throws Exception {
		// given
		ReactUserAccount reactAccount = ReactUserAccount.from( user );
		reactAccount.setFirstName( "Anton" );

		// when
		String content = Json.stringify( reactAccount );
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER + "/" + user.id() ).content( content ).contentType( "application/json" ).with( csrf() ).headers( headers ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( reactAccount ) );
		String resultContent = result.getResponse().getContentAsString();
		assertThat( resultContent ).isEqualTo( accountJson );
		Map<?, ?> map = Json.asMap( resultContent );
		Map<?, ?> account = (Map<?, ?>)map.get( "account" );
		assertThat( account.get( "firstName" ) ).isEqualTo( "Anton" );
	}

	@Test
	void testGetAircraftPage() throws Exception {
		// given
		Aircraft aftyn = new Aircraft().id( UUID.randomUUID() ).name( "AFTYN" ).type( AircraftType.FIXEDWING ).status( AircraftStatus.DESTROYED ).owner( user.id() ).ownerType( AircraftOwnerType.USER );
		Aircraft bianca = new Aircraft().id( UUID.randomUUID() ).name( "BIANCA" ).type( AircraftType.FIXEDWING ).status( AircraftStatus.DESTROYED ).owner( user.id() ).ownerType( AircraftOwnerType.USER );
		aircraftService.upsert( aftyn );
		aircraftService.upsert( bianca );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_AIRCRAFT + "/0" ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> aircraftList = (List<?>)map.get( "aircraft" );
		Map<?, ?> messagesMap = (Map<?, ?>)map.get( "messages" );

		assertThat( aircraftList.size() ).isEqualTo( 2 );
		assertThat( messagesMap ).isNull();

		Map<?, ?> aircraft0 = (Map<?, ?>)aircraftList.get( 0 );
		Map<?, ?> aircraft1 = (Map<?, ?>)aircraftList.get( 1 );
		assertThat( aircraft0.get( "name" ) ).isEqualTo( "AFTYN" );
		assertThat( aircraft1.get( "name" ) ).isEqualTo( "BIANCA" );
	}

}
