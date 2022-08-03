package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtToken;
import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtTokenProvider;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactProfileResponse;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactUser;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.DashboardServices;
import com.desertskyrangers.flightdeck.port.PublicDashboardServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.UserServices;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith( MockitoExtension.class )
public class UserControllerTest extends BaseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserServices userServices;

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private DashboardServices dashboardServices;

	@Autowired
	private PublicDashboardServices publicDashboardServices;

	@Autowired
	private JwtTokenProvider tokenProvider;

	private HttpHeaders headers;

	@BeforeEach
	protected void setup() {
		super.setup();

		userServices.setDashboardServices( dashboardServices );
		userServices.setPublicDashboardServices( publicDashboardServices );

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String jwtToken = tokenProvider.createToken( getMockUser(), authentication, false );
		headers = new HttpHeaders();
		headers.add( JwtToken.AUTHORIZATION_HEADER, JwtToken.AUTHORIZATION_TYPE + " " + jwtToken );
	}

	@AfterEach
	void teardown() {
		statePersisting.removeAllFlights();
		userServices.remove( getMockUser() );
	}

	@Test
	void testGetProfile() throws Exception {
		// when
		MvcResult result = this.mockMvc.perform( MockMvcRequestBuilders.get( ApiPath.PROFILE ).with( jwt() ).headers( headers ) ).andExpect( status().isOk() ).andReturn();

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( ReactUser.from( getMockUser() ) ) );
		assertThat( result.getResponse().getContentAsString() ).isEqualTo( accountJson );
	}

	@Test
	void testGetAccount() throws Exception {
		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER + "/" + getMockUser().id() ).with( jwt() ).headers( headers ) ).andExpect( status().isOk() ).andReturn();

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
			.perform( put( ApiPath.USER + "/" + getMockUser().id() ).content( content ).contentType( "application/json" ).with( jwt() ).headers( headers ) )
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
		String content = Json.stringify( Map.of( "id", reactAccount.getId(), "currentPassword", "password", "password", "newmockpassword" ) );
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER + "/" + getMockUser().id() + "/password" ).content( content ).contentType( "application/json" ).with( jwt() ).headers( headers ) )
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
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_AIRCRAFT ).param( "pg", "0" ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> aircraftList = (List<?>)((Map<?, ?>)map.get( "page" )).get( "content" );
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
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_BATTERY + "/0" ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

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
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_FLIGHT + "/0" ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

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
	void testGetUserMemberships() throws Exception {
		User user = getMockUser();

		Group groupA = statePersisting.upsert( new Group().name( "Group A" ).type( GroupType.CLUB ) );
		Group groupB = statePersisting.upsert( new Group().name( "Group B" ).type( GroupType.GROUP ) );

		// given
		statePersisting.upsert( new Member().user( user ).group( groupA ).status( MemberStatus.OWNER ) );
		statePersisting.upsert( new Member().user( user ).group( groupB ).status( MemberStatus.ACCEPTED ) );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_MEMBERSHIP ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> memberships = (List<?>)map.get( "memberships" );
		Map<?, ?> messagesMap = (Map<?, ?>)map.get( "messages" );

		assertThat( memberships.size() ).isEqualTo( 2 );
		assertThat( messagesMap ).isNull();
	}

	@Test
	void testGetAircraftLookup() throws Exception {
		statePersisting.upsert( createTestAircraft( getMockUser() ).status( AircraftStatus.PREFLIGHT ) );
		statePersisting.upsert( createTestAircraft( getMockUser() ).status( AircraftStatus.AIRWORTHY ) );
		statePersisting.upsert( createTestAircraft( getMockUser() ).status( AircraftStatus.INOPERATIVE ) );
		statePersisting.upsert( createTestAircraft( getMockUser() ).status( AircraftStatus.DECOMMISSIONED ) );
		statePersisting.upsert( createTestAircraft( getMockUser() ).status( AircraftStatus.DESTROYED ) );
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_AIRCRAFT_LOOKUP ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();
		List<Object> list = Json.asList( result.getResponse().getContentAsString() );
		assertThat( list.size() ).isEqualTo( 2 );
	}

	@Test
	void testGetBatteryLookup() throws Exception {
		statePersisting.upsert( createTestBattery( getMockUser() ).status( BatteryStatus.NEW ) );
		statePersisting.upsert( createTestBattery( getMockUser() ).status( BatteryStatus.AVAILABLE ) );
		statePersisting.upsert( createTestBattery( getMockUser() ).status( BatteryStatus.DESTROYED ) );
		// Plus the 'No battery specified' option
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_BATTERY_LOOKUP ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();
		List<Object> list = Json.asList( result.getResponse().getContentAsString() );
		assertThat( list.size() ).isEqualTo( 3 );
	}

	@Test
	void testGetObserverLookup() throws Exception {
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_OBSERVER_LOOKUP ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();
		List<Object> list = Json.asList( result.getResponse().getContentAsString() );
		assertThat( list.size() ).isEqualTo( 2 );
	}

	@Test
	void testGetPilotLookup() throws Exception {
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_PILOT_LOOKUP ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();
		List<Object> list = Json.asList( result.getResponse().getContentAsString() );
		assertThat( list.size() ).isEqualTo( 2 );
	}

	@Test
	void testPutMembershipAsOwner() throws Exception {
		// given
		User user = statePersisting.upsert( createTestUser( "sammy", "sammy@example.com" ) );
		Group group = statePersisting.upsert( new Group().name( "Group A" ).type( GroupType.CLUB ) );

		// Make the mock user the group owner
		statePersisting.upsert( new Member().user( getMockUser() ).group( group ).status( MemberStatus.OWNER ) );

		// when
		// Because the mock user is making the request, this is an owner request
		Map<String, String> request = Map.of( "userid", user.id().toString(), "groupid", group.id().toString(), "status", "requested" );
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER_MEMBERSHIP ).with( jwt() ).content( Json.stringify( request ) ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> memberships = (List<?>)map.get( "memberships" );
		List<?> messages = (List<?>)map.get( "messages" );

		assertThat( memberships.size() ).isEqualTo( 1 );
		assertThat( messages ).isNull();
	}

	@Test
	void testRequestMembership() throws Exception {
		// given
		User user = getMockUser();
		User owner = statePersisting.upsert( createTestUser().preferredName( "Tamara" ).email( "tammy@example.com" ) );
		Group group = statePersisting.upsert( new Group().name( "Group A" ).type( GroupType.CLUB ) );
		statePersisting.upsert( new Member().user( owner ).group( group ).status( MemberStatus.OWNER ) );

		// when
		// Because the requesting user is the mock user, this should fail
		Map<String, String> request = Map.of( "userid", user.id().toString(), "groupid", group.id().toString(), "status", "requested" );
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER_MEMBERSHIP ).with( jwt() ).content( Json.stringify( request ) ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> memberships = (List<?>)map.get( "memberships" );
		List<?> messages = (List<?>)map.get( "messages" );

		assertThat( memberships.size() ).isEqualTo( 1 );
		assertThat( messages ).isNull();
	}

	@Test
	void testInviteMembershipWhenNotOwner() throws Exception {
		// given
		User user = getMockUser();
		User owner = statePersisting.upsert( createTestUser() );
		Group group = statePersisting.upsert( new Group().name( "Group A" ).type( GroupType.CLUB ) );
		statePersisting.upsert( new Member().user( owner ).group( group ).status( MemberStatus.OWNER ) );

		// when
		// Because the requesting user is the mock user, this should fail
		Map<String, String> request = Map.of( "userid", user.id().toString(), "groupid", group.id().toString(), "status", MemberStatus.INVITED.name().toLowerCase() );
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER_MEMBERSHIP ).with( jwt() ).content( Json.stringify( request ) ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isUnauthorized() )
			.andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> memberships = (List<?>)map.get( "memberships" );
		List<?> messages = (List<?>)map.get( "messages" );

		assertThat( memberships ).isNull();
		assertThat( messages.size() ).isEqualTo( 1 );
	}

	@Test
	void testAcceptMembershipWhenNotInvited() throws Exception {
		// given
		User user = getMockUser();
		User owner = statePersisting.upsert( createTestUser() );
		Group group = statePersisting.upsert( new Group().name( "Group A" ).type( GroupType.CLUB ) );
		statePersisting.upsert( new Member().user( owner ).group( group ).status( MemberStatus.OWNER ) );

		// when
		// Because the requesting user is the mock user, this should fail
		Map<String, String> request = Map.of( "userid", user.id().toString(), "groupid", group.id().toString(), "status", MemberStatus.ACCEPTED.name().toLowerCase() );
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER_MEMBERSHIP ).with( jwt() ).content( Json.stringify( request ) ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isUnauthorized() )
			.andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> memberships = (List<?>)map.get( "memberships" );
		List<?> messages = (List<?>)map.get( "messages" );

		assertThat( memberships ).isNull();
		assertThat( messages.size() ).isEqualTo( 1 );
	}

	@Test
	void testPutMembershipWithBadRequest() throws Exception {
		// given
		User user = getMockUser();
		Group group = statePersisting.upsert( new Group().name( "Group A" ).type( GroupType.CLUB ) );
		Map<String, String> request = Map.of( "userid", user.id().toString(), "groupid", "invalid", "status", "requested" );

		// when
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER_MEMBERSHIP ).content( Json.stringify( request ) ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> memberships = (List<?>)map.get( "memberships" );
		List<?> messages = (List<?>)map.get( "messages" );

		assertThat( memberships ).isNull();
		assertThat( messages.size() ).isEqualTo( 1 );
	}

	@Test
	void testDeleteMembership() throws Exception {
		// given
		User user = getMockUser();
		Group group = statePersisting.upsert( new Group().name( "Group A" ).type( GroupType.CLUB ) );
		Member member = statePersisting.upsert( new Member().user( user ).group( group ).status( MemberStatus.ACCEPTED ) );

		Map<String, String> request = Map.of( "membershipid", member.id().toString() );

		// when
		MvcResult result = this.mockMvc
			.perform( delete( ApiPath.USER_MEMBERSHIP ).with( jwt() ).content( Json.stringify( request ) ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> memberships = (List<?>)map.get( "memberships" );
		List<?> messages = (List<?>)map.get( "messages" );

		assertThat( memberships.size() ).isEqualTo( 0 );
		assertThat( messages ).isNull();
	}

	@Test
	void testDeleteMembershipWithBadRequest() throws Exception {
		// given
		User user = getMockUser();
		Group group = statePersisting.upsert( new Group().name( "Group A" ).type( GroupType.CLUB ) );
		statePersisting.upsert( new Member().user( user ).group( group ).status( MemberStatus.ACCEPTED ) );

		Map<String, String> request = Map.of( "membershipid", "invalid" );

		// when
		MvcResult result = this.mockMvc
			.perform( delete( ApiPath.USER_MEMBERSHIP ).content( Json.stringify( request ) ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> memberships = (List<?>)map.get( "memberships" );
		List<?> messages = (List<?>)map.get( "messages" );

		assertThat( memberships ).isNull();
		assertThat( messages.size() ).isEqualTo( 1 );
	}

	@Test
	void testDashboard() throws Exception {
		// given
		dashboardServices.update( getMockUser() );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.DASHBOARD ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> dashboardMap = (Map<?, ?>)map.get( "dashboard" );
		assertThat( dashboardMap ).isNotNull();
	}

	@Test
	void testPublicDashboardWithId() throws Exception {
		// given
		publicDashboardServices.update( getMockUser() );
		userServices.setPreferences( getMockUser(), Map.of( PreferenceKey.ENABLE_PUBLIC_DASHBOARD, "true" ) );

		// when
		// NOTE - Do not send the JWT with this request. I should be anonymous.
		MvcResult result = this.mockMvc.perform( get( ApiPath.PUBLIC_DASHBOARD + "/" + getMockUser().id() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		String displayName = (String)map.get( "displayName" );
		assertThat( displayName ).isNotNull();
		assertThat( displayName ).isEqualTo( "Mock U" );
	}

	@Test
	void testPublicDashboardWithUsername() throws Exception {
		// given
		publicDashboardServices.update( getMockUser() );
		userServices.setPreferences( getMockUser(), Map.of( PreferenceKey.ENABLE_PUBLIC_DASHBOARD, "true" ) );

		// when
		// NOTE - Do not send the JWT with this request. I should be anonymous.
		MvcResult result = this.mockMvc.perform( get( ApiPath.PUBLIC_DASHBOARD + "/" + getMockUser().username() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		String displayName = (String)map.get( "displayName" );
		assertThat( displayName ).isNotNull();
		assertThat( displayName ).isEqualTo( "Mock U" );
	}

	@Test
	void testPublicDashboardWithMissingDashboard() throws Exception {
		// given

		// when
		// NOTE - Do not send the JWT with this request. I should be anonymous.
		MvcResult result = this.mockMvc.perform( get( ApiPath.PUBLIC_DASHBOARD + "/" + UUID.randomUUID() ) ).andExpect( status().isBadRequest() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> messages = (List<?>)map.get( "messages" );
		assertThat( messages ).isNotNull();
		assertThat( messages.size() ).isEqualTo( 1 );
		assertThat( messages.get( 0 ) ).isEqualTo( "Dashboard not found" );
	}

	@Test
	void testGetPreferences() throws Exception {
		// given

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_PREFERENCES ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> data = (Map<?, ?>)map.get( "data" );
		assertThat( data ).isNotNull();
	}

	@Test
	void testGetPreferencesWithNonAdminUser() throws Exception {
		// given
		User paula = statePersisting.upsert( createTestUser( "paula", "paula@example.com" ) );

		// when
		this.mockMvc.perform( get( ApiPath.USER_PREFERENCES + "/" + paula.id() ).with( jwt() ) ).andExpect( status().isForbidden() ).andReturn();
	}

	@Test
	@WithMockUser( authorities = "ADMIN" )
	void testGetPreferencesWithAdminUser() throws Exception {
		// given
		User quinn = statePersisting.upsert( createTestUser( "quinn", "quinn@example.com" ) );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_PREFERENCES + "/" + quinn.id() ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> data = (Map<?, ?>)map.get( "data" );
		assertThat( data ).isNotNull();
	}

	@Test
	void testSetPreferences() throws Exception {
		// given
		Map<String, Object> request = new HashMap<>();
		request.put( "id", getMockUser().id().toString() );
		request.put( "preferences", Map.of( "showAircraftStats", true ) );

		// then
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER_PREFERENCES ).with( jwt() ).content( Json.stringify( request ) ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> data = (Map<?, ?>)map.get( "data" );
		assertThat( data ).isNotNull();
		assertThat( data.size() ).isEqualTo( 1 );
	}

}
