package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactLocation;
import com.desertskyrangers.flightdeck.core.model.User;
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

public class LocationControllerTest extends BaseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetLocationWithSuccess() throws Exception {
		// given
		ReactLocation location = createTestReactLocation();

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.LOCATION + "/" + location.getId() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultLocation = (Map<?, ?>)map.get( "data" );
		assertThat( resultLocation.get( "name" ) ).isEqualTo( "Morning Cloak Park" );
	}

	@Test
	void testGetLocationWithBadRequest() throws Exception {
		this.mockMvc.perform( get( ApiPath.LOCATION + "/" + "bad-id" ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testNewLocationWithSuccess() throws Exception {
		ReactLocation location = createTestReactLocation();
		// This id will be overwritten
		location.setId( "new" );

		this.mockMvc.perform( post( ApiPath.LOCATION ).with( jwt() ).content( Json.stringify( location ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testNewLocationWithBadRequest() throws Exception {
		ReactLocation location = createTestReactLocation();

		location.setStatus( "not-really-a-status" );
		this.mockMvc.perform( post( ApiPath.LOCATION ).content( Json.stringify( location ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testUpdateLocationWithSuccess() throws Exception {
		ReactLocation location = createTestReactLocation();
		location.setName( "Monarch Meadows Park" );
		location.setAltitude( 1973 );

		MvcResult result = this.mockMvc.perform( put( ApiPath.LOCATION ).with( jwt() ).content( Json.stringify( location ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultLocation = (Map<?, ?>)map.get( "data" );
		assertThat( resultLocation.get( "name" ) ).isEqualTo( "Monarch Meadows Park" );
		assertThat( resultLocation.get( "altitude" ) ).isEqualTo( 1973.0 );
	}

	@Test
	void testUpdateLocationWithBadRequest() throws Exception {
		ReactLocation location = createTestReactLocation();
		location.setId( "bad" );

		this.mockMvc.perform( put( ApiPath.LOCATION ).content( Json.stringify( location ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testDeleteLocationWithSuccess() throws Exception {
		// given
		ReactLocation location = createTestReactLocation();

		// when
		MvcResult result = this.mockMvc
			.perform( delete( ApiPath.LOCATION ).content( "{\"id\":\"" + location.getId() + "\"}" ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultLocation = (Map<?, ?>)map.get( "data" );
		assertThat( resultLocation.get( "name" ) ).isEqualTo( "Morning Cloak Park" );
	}

	private ReactLocation createTestReactLocation() {
		User user = statePersisting.upsert( createTestUser() );
		return ReactLocation.from( createTestLocation( user ) );
	}

}
