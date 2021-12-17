package com.desertskyrangers.flightlog.plug.api;

import com.desertskyrangers.flightlog.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MonitorControllerTests {

	@Value( "${spring.application.version:unknown}" )
	String version;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void whenApiMonitorStatus_thenSuccessResponse() throws Exception {
		Map<String, String> response = new HashMap<>();
		response.put( "running", "true" );
		response.put( "version", version );
		this.mockMvc.perform( get( ApiPath.MONITOR_STATUS ) ).andExpect( status().isOk() ).andExpect( content().json( Json.stringify( response ), true ) );
	}

}
