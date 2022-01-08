package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraft;
import com.desertskyrangers.flightlog.core.model.*;
import com.desertskyrangers.flightlog.port.AircraftService;
import com.desertskyrangers.flightlog.port.UserService;
import com.desertskyrangers.flightlog.util.Json;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
public class UserAircraftControllerTest {

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

		this.mockMvc.perform( post( ApiPath.USER_AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
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

		this.mockMvc.perform( post( ApiPath.USER_AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
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

		this.mockMvc.perform( put( ApiPath.USER_AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
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

		this.mockMvc.perform( put( ApiPath.USER_AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

}
