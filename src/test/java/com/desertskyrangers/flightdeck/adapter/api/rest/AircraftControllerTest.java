package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactAircraft;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.AircraftService;
import com.desertskyrangers.flightdeck.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AircraftControllerTest extends BaseControllerTest {

	@Autowired
	private AircraftService aircraftService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetAircraftWithSuccess() throws Exception {
		// given
		Aircraft aircraft = createTestAircraft();
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
		ReactAircraft aircraft = createTestReactAircraft();
		aircraft.setId( "new" );

		this.mockMvc.perform( post( ApiPath.AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testNewAircraftWithBadRequest() throws Exception {
		ReactAircraft aircraft = createTestReactAircraft();
		aircraft.setId( "new" );
		aircraft.setType( "invalid" );

		this.mockMvc.perform( post( ApiPath.AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testUpdateAircraftWithSuccess() throws Exception {
		ReactAircraft aircraft = createTestReactAircraft();

		this.mockMvc.perform( put( ApiPath.AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testUpdateAircraftWithBadRequest() throws Exception {
		ReactAircraft aircraft = createTestReactAircraft();
		aircraft.setType( "invalid" );

		this.mockMvc.perform( put( ApiPath.AIRCRAFT ).content( Json.stringify( aircraft ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void deleteAircraftWithSuccess() throws Exception {
		// given
		Aircraft aircraft = createTestAircraft();
		aircraftService.upsert( aircraft );

		// when
		MvcResult result = this.mockMvc.perform( delete( ApiPath.AIRCRAFT ).content( "{\"id\":\"" + aircraft.id() + "\"}" ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?,?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?,?> resultAircraft = (Map<?,?>)map.get("aircraft");
		assertThat( resultAircraft.get("name")).isEqualTo( "Aftyn" );
	}

	private Aircraft createTestAircraft() {
		Aircraft aircraft = new Aircraft();
		aircraft.id( UUID.randomUUID() );
		aircraft.name( "Aftyn" );
		aircraft.make( "Hobby King" );
		aircraft.model( "Bixler 2" );
		aircraft.type( AircraftType.FIXEDWING );
		aircraft.status( AircraftStatus.DESTROYED );
		aircraft.owner( getUser().id() );
		aircraft.ownerType( OwnerType.USER );
		return aircraft;
	}

	private ReactAircraft createTestReactAircraft() {
		return ReactAircraft.from( createTestAircraft() );
	}
}
