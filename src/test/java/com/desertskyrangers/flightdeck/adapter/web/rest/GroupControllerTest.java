package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactGroup;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.DashboardServices;
import com.desertskyrangers.flightdeck.port.GroupServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupControllerTest extends BaseControllerTest {

	@Autowired
	private GroupServices groupServices;

	@Autowired
	private DashboardServices dashboardServices;

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testDashboard() throws Exception {
		// given
		User user = getMockUser();
		User owner = statePersisting.upsert( createTestUser() );
		Group group = statePersisting.upsert( createTestGroup() );
		statePersisting.upsert( new Member().user( owner ).group( group ).status( MemberStatus.OWNER ) );
		statePersisting.upsert( new Member().user( user ).group( group ).status( MemberStatus.ACCEPTED ) );

		// NOTE The group has to be retrieved again after adding the memberships
		dashboardServices.update( groupServices.find( group.id() ).orElse( null ) ).get();

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.GROUP + "/" + group.id() + "/dashboard" ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> data = (Map<?, ?>)map.get( "data" );
		String displayName = (String)data.get( "displayName" );
		assertThat( displayName ).isNotNull();
		assertThat( displayName ).isEqualTo( "Test Group" );
	}

	@Test
	void testInviteMemberByUsername() throws Exception {
		// given
		Group group = statePersisting.upsert( createTestGroup( "Group A", GroupType.CLUB ) );
		User invitee = statePersisting.upsert( createTestUser( "marisa", "marisa@example.com" ) );
		statePersisting.upsert( createTestToken( invitee, invitee.username(), "password" ) );
		statePersisting.upsert( new Member().user( getMockUser() ).group( group ).status( MemberStatus.OWNER ) );

		// then
		Map<String, String> request = Map.of( "id", group.id().toString(), "invitee", invitee.username() );
		MvcResult result = this.mockMvc
			.perform( post( ApiPath.GROUP_INVITE ).with( jwt() ).content( Json.stringify( request ) ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isAccepted() )
			.andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> list = (List<?>)map.get( "memberships" );
		assertThat( list ).isNotNull();
		assertThat( list.size() ).isEqualTo( 2 );
	}

	@Test
	void testInviteMemberByEmailAddress() throws Exception {
		// given
		Group group = statePersisting.upsert( createTestGroup( "Group A", GroupType.CLUB ) );
		User invitee = statePersisting.upsert( createTestUser( "marisa", "marisa@example.com" ) );
		statePersisting.upsert( createTestToken( invitee, invitee.email(), "password" ) );
		statePersisting.upsert( new Member().user( getMockUser() ).group( group ).status( MemberStatus.OWNER ) );

		// then
		Map<String, String> request = Map.of( "id", group.id().toString(), "invitee", invitee.email() );
		MvcResult result = this.mockMvc
			.perform( post( ApiPath.GROUP_INVITE ).with( jwt() ).content( Json.stringify( request ) ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isAccepted() )
			.andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> list = (List<?>)map.get( "memberships" );
		assertThat( list ).isNotNull();
		assertThat( list.size() ).isEqualTo( 2 );
	}

	@Test
	void testGetAvailableGroups() throws Exception {
		// Test get available groups for the requesting user

		// given
		Group groupA = statePersisting.upsert( createTestGroup( "Group A", GroupType.CLUB ) );
		Group groupB = statePersisting.upsert( createTestGroup( "Group B", GroupType.CLUB ) );
		Group groupC = statePersisting.upsert( createTestGroup( "Group C", GroupType.CLUB ) );
		statePersisting.upsert( new Member().user( getMockUser() ).group( groupB ).status( MemberStatus.ACCEPTED ) );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.GROUP_AVAILABLE ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		List<?> list = Json.asList( result.getResponse().getContentAsString() );
		assertThat( list ).isNotNull();
		assertThat( list.size() ).isEqualTo( 2 );
	}

	@Test
	void testGetGroupMembership() throws Exception {
		// given
		Group groupA = statePersisting.upsert( createTestGroup( "Group A", GroupType.CLUB ) );
		User frank = statePersisting.upsert( createTestUser( "frank", "frank@example.com" ) );
		User gemma = statePersisting.upsert( createTestUser( "gemma", "gemma@example.com" ) );
		User helen = statePersisting.upsert( createTestUser( "helen", "helen@example.com" ) );
		User india = statePersisting.upsert( createTestUser( "india", "india@example.com" ) );
		statePersisting.upsert( new Member().user( getMockUser() ).group( groupA ).status( MemberStatus.OWNER ) );
		statePersisting.upsert( new Member().user( frank ).group( groupA ).status( MemberStatus.REQUESTED ) );
		statePersisting.upsert( new Member().user( gemma ).group( groupA ).status( MemberStatus.INVITED ) );
		statePersisting.upsert( new Member().user( helen ).group( groupA ).status( MemberStatus.REVOKED ) );
		statePersisting.upsert( new Member().user( india ).group( groupA ).status( MemberStatus.ACCEPTED ) );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.GROUP + "/" + groupA.id() + "/membership" ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		List<?> list = Json.asList( result.getResponse().getContentAsString() );
		assertThat( list ).isNotNull();
		assertThat( list.size() ).isEqualTo( 5 );
	}

	@Test
	void getGroupWithSuccess() throws Exception {
		// given
		Group group = createTestGroup();
		statePersisting.upsert( group );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.GROUP + "/" + group.id() ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultGroup = (Map<?, ?>)map.get( "group" );
		assertThat( resultGroup.get( "name" ) ).isEqualTo( group.name() );
	}

	@Test
	void getGroupWithBadRequest() throws Exception {
		this.mockMvc.perform( get( ApiPath.GROUP + "/" + "bad-id" ).with( jwt() ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testNewGroupWithSuccess() throws Exception {
		ReactGroup group = createTestReactGroup();
		group.setId( "new" );

		this.mockMvc.perform( post( ApiPath.GROUP ).with( jwt() ).content( Json.stringify( group ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testNewGroupWithBadRequest() throws Exception {
		ReactGroup group = createTestReactGroup();
		group.setId( "new" );
		group.setType( "invalid" );

		this.mockMvc.perform( post( ApiPath.GROUP ).with( jwt() ).content( Json.stringify( group ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testUpdateGroupWithSuccess() throws Exception {
		ReactGroup group = createTestReactGroup();

		this.mockMvc.perform( put( ApiPath.GROUP ).with( jwt() ).content( Json.stringify( group ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testUpdateGroupWithBadRequest() throws Exception {
		ReactGroup group = createTestReactGroup();
		group.setType( "invalid" );

		this.mockMvc.perform( put( ApiPath.GROUP ).with( jwt() ).content( Json.stringify( group ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void deleteGroupWithSuccess() throws Exception {
		// given
		Group group = createTestGroup();
		statePersisting.upsert( group );

		// when
		MvcResult result = this.mockMvc
			.perform( delete( ApiPath.GROUP ).with( jwt() ).content( "{\"id\":\"" + group.id() + "\"}" ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultGroup = (Map<?, ?>)map.get( "group" );
		assertThat( resultGroup.get( "id" ) ).isEqualTo( group.id().toString() );
	}

	private ReactGroup createTestReactGroup() {
		return ReactGroup.from( createTestGroup() );
	}

}
