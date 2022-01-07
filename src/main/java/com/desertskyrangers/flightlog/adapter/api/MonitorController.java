package com.desertskyrangers.flightlog.adapter.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class MonitorController {

	@Value("${spring.application.version:unknown}") String version;

	@GetMapping( ApiPath.MONITOR_STATUS )
	public Map<String, String> whenApiMonitorStatus_thenSuccessResponse() {
		Map<String, String> response = new HashMap<>();
		response.put( "running", "true" );
		response.put( "version", version );
		return response;
	}

}
