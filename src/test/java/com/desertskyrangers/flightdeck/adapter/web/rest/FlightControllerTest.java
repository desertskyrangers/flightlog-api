package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactFlight;
import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.core.model.User;
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

public class FlightControllerTest extends BaseControllerTest {

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getFlightWithSuccess() throws Exception {
		// given
		Flight flight = createTestFlight( getMockUser() );
		statePersisting.upsert( flight );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.FLIGHT + "/" + flight.id() ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultFlight = (Map<?, ?>)map.get( "flight" );
		assertThat( resultFlight.get( "timestamp" ) ).isEqualTo( flight.timestamp() );

		assertThat( resultFlight.get( "location" ) ).isEqualTo( flight.location().id().toString() );
		assertThat( resultFlight.get( "latitude" ) ).isEqualTo( flight.latitude() );
		assertThat( resultFlight.get( "longitude" ) ).isEqualTo( flight.longitude() );
		assertThat( resultFlight.get( "altitude" ) ).isEqualTo( flight.altitude() );
	}

	@Test
	void getFlightWithCustomLocation() throws Exception {
		// given
		Flight flight = createTestFlight( getMockUser() );
		flight.location( Location.CUSTOM_LOCATION );
		statePersisting.upsert( flight );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.FLIGHT + "/" + flight.id() ).with( jwt() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultFlight = (Map<?, ?>)map.get( "flight" );
		assertThat( resultFlight.get( "timestamp" ) ).isEqualTo( flight.timestamp() );

		assertThat( resultFlight.get( "location" ) ).isEqualTo( flight.location().id().toString() );
		assertThat( resultFlight.get( "latitude" ) ).isEqualTo( flight.latitude() );
		assertThat( resultFlight.get( "longitude" ) ).isEqualTo( flight.longitude() );
		assertThat( resultFlight.get( "altitude" ) ).isEqualTo( flight.altitude() );
	}

	@Test
	void getFlightWithBadRequest() throws Exception {
		this.mockMvc.perform( get( ApiPath.FLIGHT + "/" + "bad-id" ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testNewFlightWithSuccess() throws Exception {
		ReactFlight flight = createTestReactFlight();
		flight.setId( "new" );

		this.mockMvc.perform( post( ApiPath.FLIGHT ).with( jwt() ).content( Json.stringify( flight ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testNewFlightWithBadRequest() throws Exception {
		ReactFlight flight = createTestReactFlight();
		flight.setId( "new" );
		flight.setAircraft( "invalid" );

		this.mockMvc.perform( post( ApiPath.FLIGHT ).with( jwt() ).content( Json.stringify( flight ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testUpdateFlightWithSuccess() throws Exception {
		ReactFlight flight = createTestReactFlight();

		this.mockMvc.perform( put( ApiPath.FLIGHT ).with( jwt() ).content( Json.stringify( flight ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testUpdateFlightWithBadRequest() throws Exception {
		ReactFlight flight = createTestReactFlight();
		flight.setAircraft( "invalid" );

		this.mockMvc.perform( put( ApiPath.FLIGHT ).with( jwt() ).content( Json.stringify( flight ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void deleteFlightWithSuccess() throws Exception {
		// given
		Flight flight = createTestFlight( getMockUser() );
		statePersisting.upsert( flight );

		// when
		MvcResult result = this.mockMvc
			.perform( delete( ApiPath.FLIGHT ).with( jwt() ).content( "{\"id\":\"" + flight.id() + "\"}" ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultFlight = (Map<?, ?>)map.get( "flight" );
		assertThat( resultFlight.get( "id" ) ).isEqualTo( flight.id().toString() );
		assertThat( resultFlight.get( "location" ) ).isNotNull();
		assertThat( resultFlight.get( "latitude" ) ).isEqualTo( flight.latitude() );
		assertThat( resultFlight.get( "longitude" ) ).isEqualTo( flight.longitude() );
		assertThat( resultFlight.get( "altitude" ) ).isEqualTo( flight.altitude() );
	}

	@Test
	void deleteFlightWithSuccessAndCustomLocation() throws Exception {
		// given
		Flight flight = createTestFlight( getMockUser() );
		flight.location( null );
		flight.latitude( 40.50353298117737 );
		flight.longitude( -112.01466589278837 );
		flight.altitude( 1478 );

		statePersisting.upsert( flight );

		// when
		MvcResult result = this.mockMvc
			.perform( delete( ApiPath.FLIGHT ).with( jwt() ).content( "{\"id\":\"" + flight.id() + "\"}" ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultFlight = (Map<?, ?>)map.get( "flight" );
		assertThat( resultFlight.get( "id" ) ).isEqualTo( flight.id().toString() );
		assertThat( resultFlight.get( "location" ) ).isNull();
		assertThat( resultFlight.get( "latitude" ) ).isEqualTo( flight.latitude() );
		assertThat( resultFlight.get( "longitude" ) ).isEqualTo( flight.longitude() );
		assertThat( resultFlight.get( "altitude" ) ).isEqualTo( flight.altitude() );
	}

	private ReactFlight createTestReactFlight() {
		return createTestReactFlight( getMockUser(), getMockUser() );
	}

	private ReactFlight createTestReactFlight( User requester, User pilot ) {
		return ReactFlight.from( requester, createTestFlight( pilot ) );
	}

}
