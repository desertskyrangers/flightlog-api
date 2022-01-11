package com.desertskyrangers.flightdeck.adapter.api;

import com.desertskyrangers.flightdeck.adapter.api.model.ReactAircraft;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.AircraftService;
import com.desertskyrangers.flightdeck.port.UserService;
import com.desertskyrangers.flightdeck.util.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
public class AircraftControllerTest {

	@Autowired
	private AircraftService aircraftService;

	@Autowired
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	private User user;

	@BeforeEach
	void setup() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if( authentication != null ) {
			String username = authentication.getName();

			// Delete the exising mock user account
			userService.findByPrincipal( authentication.getName() ).ifPresent( u -> userService.remove( u ) );

			// Create mock user account
			user = new User();
			UserToken token = new UserToken().principal( username );
			user.tokens( Set.of( token ) );
			token.user( user );
			userService.upsert( user );
		}
	}

	@Test
	void getAircraftWithSuccess() throws Exception {
		// given
		Aircraft aircraft = new Aircraft();
		aircraft.id( UUID.randomUUID() );
		aircraft.name( "Aftyn" );
		aircraft.make( "Hobby King" );
		aircraft.model( "Bixler 2" );
		aircraft.type( AircraftType.FIXEDWING );
		aircraft.status( AircraftStatus.DESTROYED );
		aircraft.owner( user.id() );
		aircraft.ownerType( AircraftOwnerType.USER );
		aircraftService.upsert( aircraft );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.AIRCRAFT + "/" + aircraft.id() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?,?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?,?> resultAircraft = (Map<?,?>)map.get("aircraft");
		assertThat( resultAircraft.get("name")).isEqualTo( "Aftyn" );
	}

	@Test
	void testNewAircraftWithSuccess() throws Exception {
		ReactAircraft aircraft = new ReactAircraft();
		aircraft.setId( "new" );
		aircraft.setName( "Aftyn" );
		aircraft.setMake( "Hobby King" );
		aircraft.setModel( "Bixler 2" );
		aircraft.setType( AircraftType.FIXEDWING.name().toLowerCase() );
		aircraft.setStatus( AircraftStatus.DESTROYED.name().toLowerCase() );
		aircraft.setOwner( user.id().toString() );
		aircraft.setOwnerType( AircraftOwnerType.USER.name().toLowerCase() );

		this.mockMvc.perform( post( ApiPath.AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testNewAircraftWithBadRequest() throws Exception {
		ReactAircraft aircraft = new ReactAircraft();
		aircraft.setId( "new" );
		aircraft.setName( "Aftyn" );
		aircraft.setMake( "Hobby King" );
		aircraft.setModel( "Bixler 2" );
		aircraft.setType( "invalid" );
		aircraft.setStatus( AircraftStatus.DESTROYED.name().toLowerCase() );
		aircraft.setOwner( user.id().toString() );
		aircraft.setOwnerType( AircraftOwnerType.USER.name().toLowerCase() );

		this.mockMvc.perform( post( ApiPath.AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testUpdateAircraftWithSuccess() throws Exception {
		ReactAircraft aircraft = new ReactAircraft();
		aircraft.setId( UUID.randomUUID().toString() );
		aircraft.setName( "Aftyn" );
		aircraft.setMake( "Hobby King" );
		aircraft.setModel( "Bixler 2" );
		aircraft.setType( AircraftType.FIXEDWING.name().toLowerCase() );
		aircraft.setStatus( AircraftStatus.DESTROYED.name().toLowerCase() );
		aircraft.setOwner( user.id().toString() );
		aircraft.setOwnerType( AircraftOwnerType.USER.name().toLowerCase() );

		this.mockMvc.perform( put( ApiPath.AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testUpdateAircraftWithBadRequest() throws Exception {
		ReactAircraft aircraft = new ReactAircraft();
		aircraft.setId( UUID.randomUUID().toString() );
		aircraft.setName( "Aftyn" );
		aircraft.setMake( "Hobby King" );
		aircraft.setModel( "Bixler 2" );
		aircraft.setType( "invalid" );
		aircraft.setStatus( AircraftStatus.DESTROYED.name().toLowerCase() );
		aircraft.setOwner( user.id().toString() );
		aircraft.setOwnerType( AircraftOwnerType.USER.name().toLowerCase() );

		this.mockMvc.perform( put( ApiPath.AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void deleteAircraftWithSuccess() throws Exception {
		// given
		Aircraft aircraft = new Aircraft();
		aircraft.id( UUID.randomUUID() );
		aircraft.name( "Aftyn" );
		aircraft.make( "Hobby King" );
		aircraft.model( "Bixler 2" );
		aircraft.type( AircraftType.FIXEDWING );
		aircraft.status( AircraftStatus.DESTROYED );
		aircraft.owner( user.id() );
		aircraft.ownerType( AircraftOwnerType.USER );
		aircraftService.upsert( aircraft );

		// when
		MvcResult result = this.mockMvc.perform( delete( ApiPath.AIRCRAFT ).content( "{\"id\":\"" + aircraft.id() + "\"}" ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?,?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?,?> resultAircraft = (Map<?,?>)map.get("aircraft");
		assertThat( resultAircraft.get("name")).isEqualTo( "Aftyn" );
	}

}
