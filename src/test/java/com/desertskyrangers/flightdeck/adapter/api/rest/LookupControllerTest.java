package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactAircraftStatus;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactAircraftType;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactSmsCarrier;
import com.desertskyrangers.flightdeck.core.model.AircraftStatus;
import com.desertskyrangers.flightdeck.core.model.AircraftType;
import com.desertskyrangers.flightdeck.core.model.SmsCarrier;
import com.desertskyrangers.flightdeck.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
public class LookupControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetAircraftStatuses() throws Exception {
		List<ReactAircraftStatus> response = Arrays.stream( AircraftStatus.values() ).map( c -> new ReactAircraftStatus( c.name().toLowerCase(), c.getName() ) ).toList();
		this.mockMvc.perform( MockMvcRequestBuilders.get( ApiPath.AIRCRAFT_STATUS ) ).andExpect( status().isOk() ).andExpect( content().json( Json.stringify( response ), true ) );
	}

	@Test
	void testGetAircraftTypes() throws Exception {
		List<ReactAircraftType> response = Arrays.stream( AircraftType.values() ).map( c -> new ReactAircraftType( c.name().toLowerCase(), c.getName() ) ).toList();
		this.mockMvc.perform( get( ApiPath.AIRCRAFT_TYPE ) ).andExpect( status().isOk() ).andExpect( content().json( Json.stringify( response ), true ) );
	}

	@Test
	void testGetSmsCarriers() throws Exception {
		List<ReactSmsCarrier> response = Arrays.stream( SmsCarrier.values() ).map( c -> new ReactSmsCarrier( c.name().toLowerCase(), c.getName() ) ).toList();
		this.mockMvc.perform( get( ApiPath.SMS_CARRIERS ) ).andExpect( status().isOk() ).andExpect( content().json( Json.stringify( response ), true ) );
	}

}
