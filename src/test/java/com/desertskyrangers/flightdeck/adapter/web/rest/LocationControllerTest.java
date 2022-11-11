package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactLocation;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.port.LocationServices;
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
	private LocationServices locationServices;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetLocationWithSuccess() throws Exception {
		// given
		Location location = createTestLocation();
		locationServices.upsert( location );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.LOCATION + "/" + location.id() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultLocation = (Map<?, ?>)map.get( "data" );
		assertThat( resultLocation.get( "name" ) ).isEqualTo( "Morning Cloak Park" );
	}

	@Test
	void getLocationWithBadRequest() throws Exception {
		this.mockMvc.perform( get( ApiPath.LOCATION + "/" + "bad-id" ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testNewLocationWithSuccess() throws Exception {
		ReactLocation location = createTestReactLocation();
		location.setId( "new" );

		this.mockMvc.perform( post( ApiPath.LOCATION ).content( Json.stringify( location ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testNewLocationWithBadRequest() throws Exception {
		ReactLocation location = createTestReactLocation();

		//location.setStatus( "not-really-a-status" );
		//this.mockMvc.perform( post( ApiPath.LOCATION ).content( Json.stringify( location ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testUpdateLocationWithSuccess() throws Exception {
		ReactLocation location = createTestReactLocation();
		location.setName( "Monarch Meadows Park" );

		MvcResult result = this.mockMvc.perform( put( ApiPath.LOCATION ).content( Json.stringify( location ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultLocation = (Map<?, ?>)map.get( "data" );
		assertThat( resultLocation.get( "name" ) ).isEqualTo( "Monarch Meadows Park" );
	}

	@Test
	void testUpdateLocationWithBadRequest() throws Exception {
		ReactLocation location = createTestReactLocation();
		location.setId( "bad" );

		this.mockMvc.perform( put( ApiPath.LOCATION ).content( Json.stringify( location ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void deleteLocationWithSuccess() throws Exception {
		// given
		Location location = createTestLocation();
		locationServices.upsert( location );

		// when
		MvcResult result = this.mockMvc
			.perform( delete( ApiPath.LOCATION ).content( "{\"id\":\"" + location.id() + "\"}" ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultLocation = (Map<?, ?>)map.get( "data" );
		assertThat( resultLocation.get( "name" ) ).isEqualTo( "Morning Cloak Park" );
	}

	private ReactLocation createTestReactLocation() {
		return ReactLocation.from( createTestLocation() );
	}

}
