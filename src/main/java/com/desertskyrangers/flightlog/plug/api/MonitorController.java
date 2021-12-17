package com.desertskyrangers.flightlog.plug.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MonitorController {

	@Value("${spring.application.version:unknown}") String version;

	@GetMapping( ApiPath.MONITOR_STATUS )
	public Map<String, String> isAlive() {
		Map<String, String> response = new HashMap<>();
		response.put( "running", "true" );
		response.put( "version", version );
		return response;
	}

}
