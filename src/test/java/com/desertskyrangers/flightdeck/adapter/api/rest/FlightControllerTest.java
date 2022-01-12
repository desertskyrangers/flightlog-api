package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.core.model.Aircraft;
import com.desertskyrangers.flightdeck.core.model.Battery;
import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.port.AircraftService;
import com.desertskyrangers.flightdeck.port.BatteryService;
import com.desertskyrangers.flightdeck.port.FlightService;
import com.desertskyrangers.flightdeck.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FlightControllerTest extends BaseControllerTest {

	@Autowired
	private AircraftService aircraftService;

	@Autowired
	private BatteryService batteryService;

	@Autowired
	private FlightService flightService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getFlightWithSuccess() throws Exception {
		// given
		Flight flight = createTestFlight();
		flightService.upsert( flight );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.FLIGHT + "/" + flight.id() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultFlight = (Map<?, ?>)map.get( "flight" );
		assertThat( resultFlight.get( "timestamp" ) ).isEqualTo( flight.timestamp() );
	}

	private Flight createTestFlight() {
		Aircraft aircraft = createTestAircraft();
		aircraftService.upsert(aircraft);
		Battery battery = createTestBattery();
		batteryService.upsert(battery);
		Flight flight = new Flight();
		flight.pilot( getMockUser());
		//flight.observer( "Oscar Observer");
		flight.aircraft( aircraft );
		flight.batteries( Set.of( battery ) );
		flight.timestamp( System.currentTimeMillis() );
		flight.duration(1000);
		flight.notes("Just a test flight");
		return flight;
	}

//	private ReactFlight createTestReactFlight() {
//		return ReactFlight.toFlight( createTestFlight() );
//	}

}
