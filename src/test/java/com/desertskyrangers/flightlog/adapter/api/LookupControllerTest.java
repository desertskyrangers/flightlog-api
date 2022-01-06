package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactSmsCarrier;
import com.desertskyrangers.flightlog.core.model.SmsCarrier;
import com.desertskyrangers.flightlog.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
	void testGetSmsCarriers() throws Exception {
		List<ReactSmsCarrier> response = Arrays.stream( SmsCarrier.values() ).map( c -> new ReactSmsCarrier( c.name().toLowerCase(), c.getTitle() ) ).toList();
		this.mockMvc.perform( get( ApiPath.SMS_CARRIERS ) ).andExpect( status().isOk() ).andExpect( content().json( Json.stringify( response ), true ) );
	}

}
