package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactGroup;
import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupControllerTest extends BaseControllerTest {

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getGroupWithSuccess() throws Exception {
		// given
		Group group = createTestGroup();
		statePersisting.upsert( group );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.GROUP + "/" + group.id() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultGroup = (Map<?, ?>)map.get( "group" );
		assertThat( resultGroup.get( "name" ) ).isEqualTo( group.name() );
	}

	@Test
	void getGroupWithBadRequest() throws Exception {
		this.mockMvc.perform( get( ApiPath.GROUP + "/" + "bad-id" ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testNewGroupWithSuccess() throws Exception {
		ReactGroup group = createTestReactGroup();
		group.setId( "new" );

		this.mockMvc.perform( post( ApiPath.GROUP ).content( Json.stringify( group ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testNewGroupWithBadRequest() throws Exception {
		ReactGroup group = createTestReactGroup();
		group.setId( "new" );
		group.setType( "invalid" );

		this.mockMvc.perform( post( ApiPath.GROUP ).content( Json.stringify( group ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testUpdateGroupWithSuccess() throws Exception {
		ReactGroup group = createTestReactGroup();

		this.mockMvc.perform( put( ApiPath.GROUP ).content( Json.stringify( group ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testUpdateGroupWithBadRequest() throws Exception {
		ReactGroup group = createTestReactGroup();
		group.setType( "invalid" );

		this.mockMvc.perform( put( ApiPath.GROUP ).content( Json.stringify( group ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void deleteGroupWithSuccess() throws Exception {
		// given
		Group group = createTestGroup();
		statePersisting.upsert( group );

		// when
		MvcResult result = this.mockMvc.perform( delete( ApiPath.GROUP ).content( "{\"id\":\"" + group.id() + "\"}" ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultGroup = (Map<?, ?>)map.get( "group" );
		assertThat( resultGroup.get( "id" ) ).isEqualTo( group.id().toString() );
	}

	private ReactGroup createTestReactGroup() {
		return ReactGroup.from( createTestGroup() );
	}

}
