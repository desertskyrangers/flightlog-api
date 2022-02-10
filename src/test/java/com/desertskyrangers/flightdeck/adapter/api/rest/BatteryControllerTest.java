package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactBattery;
import com.desertskyrangers.flightdeck.core.model.Battery;
import com.desertskyrangers.flightdeck.port.BatteryServices;
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

public class BatteryControllerTest extends BaseControllerTest {

	@Autowired
	private BatteryServices batteryServices;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getBatteryWithSuccess() throws Exception {
		// given
		Battery battery = createTestBattery( getMockUser() );
		batteryServices.upsert( battery );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.BATTERY + "/" + battery.id() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultBattery = (Map<?, ?>)map.get( "battery" );
		assertThat( resultBattery.get( "name" ) ).isEqualTo( "C 4S 2650 Turnigy" );
	}

	@Test
	void getBatteryWithBadRequest() throws Exception {
		this.mockMvc.perform( get( ApiPath.BATTERY + "/" + "bad-id" ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testNewBatteryWithSuccess() throws Exception {
		ReactBattery battery = createTestReactBattery();
		battery.setId( "new" );

		this.mockMvc.perform( post( ApiPath.BATTERY ).content( Json.stringify( battery ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testNewBatteryWithBadRequest() throws Exception {
		ReactBattery battery = createTestReactBattery();
		battery.setId( "new" );
		battery.setType( "invalid" );

		this.mockMvc.perform( post( ApiPath.BATTERY ).content( Json.stringify( battery ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void testUpdateBatteryWithSuccess() throws Exception {
		ReactBattery battery = createTestReactBattery();

		this.mockMvc.perform( put( ApiPath.BATTERY ).content( Json.stringify( battery ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();
	}

	@Test
	void testUpdateBatteryWithBadRequest() throws Exception {
		ReactBattery battery = createTestReactBattery();
		battery.setType( "invalid" );

		this.mockMvc.perform( put( ApiPath.BATTERY ).content( Json.stringify( battery ) ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andReturn();
	}

	@Test
	void deleteBatteryWithSuccess() throws Exception {
		// given
		Battery battery = createTestBattery( getMockUser() );
		batteryServices.upsert( battery );

		// when
		MvcResult result = this.mockMvc
			.perform( delete( ApiPath.BATTERY ).content( "{\"id\":\"" + battery.id() + "\"}" ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultBattery = (Map<?, ?>)map.get( "battery" );
		assertThat( resultBattery.get( "name" ) ).isEqualTo( "C 4S 2650 Turnigy" );
	}

	private ReactBattery createTestReactBattery() {
		return ReactBattery.from( createTestBattery( getMockUser() ) );
	}

}
