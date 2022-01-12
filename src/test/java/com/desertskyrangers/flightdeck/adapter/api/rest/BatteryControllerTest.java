package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactBattery;
import com.desertskyrangers.flightdeck.core.model.Battery;
import com.desertskyrangers.flightdeck.core.model.BatteryStatus;
import com.desertskyrangers.flightdeck.core.model.BatteryType;
import com.desertskyrangers.flightdeck.core.model.OwnerType;
import com.desertskyrangers.flightdeck.port.BatteryService;
import com.desertskyrangers.flightdeck.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BatteryControllerTest extends BaseControllerTest {

	@Autowired
	private BatteryService batteryService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getBatteryWithSuccess() throws Exception {
		// given
		Battery battery = createTestBattery();
		batteryService.upsert( battery );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.BATTERY + "/" + battery.id() ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultBattery = (Map<?, ?>)map.get( "battery" );
		assertThat( resultBattery.get( "name" ) ).isEqualTo( "C 4S 2650 Turnigy" );
	}

	@Test
	void getBatteryWithBadRequest() throws Exception {
		// given
		Battery battery = createTestBattery();
		batteryService.upsert( battery );

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.BATTERY + "/" + "bad-id" ) ).andExpect( status().isOk() ).andReturn();

		// then
		Map<?, ?> map = Json.asMap( result.getResponse().getContentAsString() );
		Map<?, ?> resultBattery = (Map<?, ?>)map.get( "battery" );
		assertThat( resultBattery.get( "name" ) ).isEqualTo( "C 4S 2650 Turnigy" );
	}

	@Test
	void testNewBatteryWithSuccess() throws Exception {
		ReactBattery battery = new ReactBattery();
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

	private Battery createTestBattery() {
		Battery battery = new Battery();
		battery.name( "C 4S 2650 Turnigy" );
		battery.make( "Hobby King" );
		battery.model( "Turnigy 2650 4S" );
		battery.connector( "XT60" );
		battery.status( BatteryStatus.DESTROYED );

		battery.type( BatteryType.LIPO );
		battery.cells( 4 );
		battery.cycles( 57 );
		battery.capacity( 2650 );
		battery.chargeRating( 1 );
		battery.dischargeRating( 20 );

		battery.owner( getUser().id() );
		battery.ownerType( OwnerType.USER );
		return battery;
	}

	private ReactBattery createTestReactBattery() {
		return ReactBattery.from( createTestBattery() );
	}
}
