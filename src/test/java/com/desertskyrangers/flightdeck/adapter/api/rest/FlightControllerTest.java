package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactFlight;
import com.desertskyrangers.flightdeck.core.model.Aircraft;
import com.desertskyrangers.flightdeck.core.model.Battery;
import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FlightControllerTest extends BaseControllerTest {

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getFlightWithSuccess() throws Exception {
		// given
		Flight flight = createTestFlight();
		statePersisting.upsert( flight );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.FLIGHT + "/" + flight.id() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultFlight = (Map<?, ?>)map.get( "flight" );
		assertThat( resultFlight.get( "timestamp" ) ).isEqualTo( flight.timestamp() );
	}

	@Test
	void getFlightWithBadRequest() throws Exception {
		this.mockMvc.perform( get( ApiPath.FLIGHT + "/" + "bad-id" ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testNewFlightWithSuccess() throws Exception {
		ReactFlight flight = createTestReactFlight();
		flight.setId( "new" );

		this.mockMvc.perform( post( ApiPath.FLIGHT ).content( Json.stringify( flight ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testNewFlightWithBadRequest() throws Exception {
		ReactFlight flight = createTestReactFlight();
		flight.setId( "new" );
		flight.setAircraft( "invalid" );

		this.mockMvc.perform( post( ApiPath.FLIGHT ).content( Json.stringify( flight ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testUpdateFlightWithSuccess() throws Exception {
		ReactFlight flight = createTestReactFlight();

		this.mockMvc.perform( put( ApiPath.FLIGHT ).content( Json.stringify( flight ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testUpdateFlightWithBadRequest() throws Exception {
		ReactFlight flight = createTestReactFlight();
		flight.setAircraft( "invalid" );

		this.mockMvc.perform( put( ApiPath.FLIGHT ).content( Json.stringify( flight ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void deleteFlightWithSuccess() throws Exception {
		// given
		Flight flight = createTestFlight();
		statePersisting.upsert( flight );

		// when
		MvcResult result = this.mockMvc
			.perform( delete( ApiPath.FLIGHT ).content( "{\"id\":\"" + flight.id() + "\"}" ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultFlight = (Map<?, ?>)map.get( "flight" );
		assertThat( resultFlight.get( "id" ) ).isEqualTo( flight.id().toString() );
	}

	private ReactFlight createTestReactFlight() {
		return ReactFlight.from( createTestFlight() );
	}

}
