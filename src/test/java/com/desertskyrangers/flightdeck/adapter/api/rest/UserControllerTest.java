package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.jwt.JwtToken;
import com.desertskyrangers.flightdeck.adapter.api.jwt.JwtTokenProvider;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactProfileResponse;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactUser;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.StatePersisting;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith( MockitoExtension.class )
public class UserControllerTest extends BaseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private JwtTokenProvider tokenProvider;

	//private User user;

	private HttpHeaders headers;

	@BeforeEach
	void setup() {
		super.setup();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String jwtToken = tokenProvider.createToken( getMockUser(), authentication, false );
		headers = new HttpHeaders();
		headers.add( JwtToken.AUTHORIZATION_HEADER, JwtToken.AUTHORIZATION_TYPE + " " + jwtToken );
	}

	@AfterEach
	void teardown() {
		statePersisting.removeAllFlights();
		userService.remove( getMockUser() );
	}

	@Test
	void testGetProfile() throws Exception {
		// when
		MvcResult result = this.mockMvc.perform( MockMvcRequestBuilders.get( ApiPath.PROFILE ).with( csrf() ).headers( headers ) ).andExpect( status().isOk() ).andReturn();

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( ReactUser.from( getMockUser() ) ) );
		assertThat( result.getResponse().getContentAsString() ).isEqualTo( accountJson );
	}

	@Test
	void testGetAccount() throws Exception {
		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER + "/" + getMockUser().id() ).with( csrf() ).headers( headers ) ).andExpect( status().isOk() ).andReturn();

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( ReactUser.from( getMockUser() ) ) );
		assertThat( result.getResponse().getContentAsString() ).isEqualTo( accountJson );
	}

	@Test
	void testUpdateAccount() throws Exception {
		// given
		ReactUser reactAccount = ReactUser.from( getMockUser() );
		reactAccount.setFirstName( "Anton" );

		// when
		String content = Json.stringify( reactAccount );
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER + "/" + getMockUser().id() ).content( content ).contentType( "application/json" ).with( csrf() ).headers( headers ) )
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
	void testUpdatePassword() throws Exception {
		// given
		ReactUser reactAccount = ReactUser.from( getMockUser() );

		// when
		String content = Json.stringify( Map.of("id", reactAccount.getId(), "currentPassword", "password", "password", "newmockpassword"));
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER + "/" + getMockUser().id() + "/password" ).content( content ).contentType( "application/json" ).with( csrf() ).headers( headers ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
	}

	@Test
	void testGetAircraftPage() throws Exception {
		// given
		Aircraft aftyn = new Aircraft().name( "AFTYN" ).type( AircraftType.FIXEDWING ).status( AircraftStatus.DESTROYED ).owner( getMockUser().id() ).ownerType( OwnerType.USER );
		Aircraft bianca = new Aircraft().name( "BIANCA" ).type( AircraftType.FIXEDWING ).status( AircraftStatus.DESTROYED ).owner( getMockUser().id() ).ownerType( OwnerType.USER );
		statePersisting.upsert( aftyn );
		statePersisting.upsert( bianca );

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
	void testGetBatteryPage() throws Exception {
		// given
		Battery a = new Battery().name( "A" ).status( BatteryStatus.NEW ).owner( getMockUser().id() ).ownerType( OwnerType.USER );
		Battery b = new Battery().name( "B" ).status( BatteryStatus.NEW ).owner( getMockUser().id() ).ownerType( OwnerType.USER );
		statePersisting.upsert( a );
		statePersisting.upsert( b );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_BATTERY + "/0" ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> batteryList = (List<?>)map.get( "batteries" );
		Map<?, ?> messagesMap = (Map<?, ?>)map.get( "messages" );

		assertThat( batteryList.size() ).isEqualTo( 2 );
		assertThat( messagesMap ).isNull();

		Map<?, ?> battery0 = (Map<?, ?>)batteryList.get( 0 );
		Map<?, ?> battery1 = (Map<?, ?>)batteryList.get( 1 );
		assertThat( battery0.get( "name" ) ).isEqualTo( "A" );
		assertThat( battery1.get( "name" ) ).isEqualTo( "B" );
	}

	@Test
	void testGetFlightPage() throws Exception {
		// given
		Aircraft aftyn = new Aircraft().name( "AFTYN" ).type( AircraftType.FIXEDWING ).status( AircraftStatus.DESTROYED ).owner( getMockUser().id() ).ownerType( OwnerType.USER );
		Battery batteryA = new Battery().name( "A" ).status( BatteryStatus.NEW ).owner( getMockUser().id() ).ownerType( OwnerType.USER );
		statePersisting.upsert( aftyn );
		statePersisting.upsert( batteryA );
		Flight flightA = new Flight().pilot( getMockUser() ).aircraft( aftyn ).batteries( Set.of( batteryA ) );
		Flight flightB = new Flight().pilot( getMockUser() ).aircraft( aftyn ).batteries( Set.of( batteryA ) );
		statePersisting.upsert( flightA );
		statePersisting.upsert( flightB );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_FLIGHT + "/0" ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> flightList = (List<?>)map.get( "flights" );
		Map<?, ?> messagesMap = (Map<?, ?>)map.get( "messages" );

		assertThat( flightList.size() ).isEqualTo( 2 );
		assertThat( messagesMap ).isNull();

		Map<?, ?> flight0 = (Map<?, ?>)flightList.get( 0 );
		Map<?, ?> flight1 = (Map<?, ?>)flightList.get( 1 );
		assertThat( flight0.get( "aircraft" ) ).isEqualTo( aftyn.id().toString() );
		assertThat( flight1.get( "aircraft" ) ).isEqualTo( aftyn.id().toString() );
	}

	@Test
	void testGetAircraftLookup() throws Exception {
		statePersisting.upsert( createTestAircraft() );
		statePersisting.upsert( createTestAircraft() );
		statePersisting.upsert( createTestAircraft() );
		statePersisting.upsert( createTestAircraft() );
		statePersisting.upsert( createTestAircraft() );
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_AIRCRAFT_LOOKUP ) ).andExpect( status().isOk() ).andReturn();
		List<Object> list = Json.asList( result.getResponse().getContentAsString() );
		assertThat( list.size() ).isEqualTo( 5 );
	}

	@Test
	void testGetBatteryLookup() throws Exception {
		statePersisting.upsert( createTestBattery() );
		statePersisting.upsert( createTestBattery() );
		statePersisting.upsert( createTestBattery() );
		// Plus the 'No battery specified' option
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_BATTERY_LOOKUP ) ).andExpect( status().isOk() ).andReturn();
		List<Object> list = Json.asList( result.getResponse().getContentAsString() );
		assertThat( list.size() ).isEqualTo( 4 );
	}

	@Test
	void testGetObserverLookup() throws Exception {
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_OBSERVER_LOOKUP ) ).andExpect( status().isOk() ).andReturn();
		List<Object> list = Json.asList( result.getResponse().getContentAsString() );
		assertThat( list.size() ).isEqualTo( 2 );
	}

	@Test
	void testGetPilotLookup() throws Exception {
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_PILOT_LOOKUP ) ).andExpect( status().isOk() ).andReturn();
		List<Object> list = Json.asList( result.getResponse().getContentAsString() );
		assertThat( list.size() ).isEqualTo( 2 );
	}

}
