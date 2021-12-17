package com.desertskyrangers.flightlog.plug.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

	@GetMapping("/status")
	public Map<String,String> isAlive() {
		return Map.of( "running", "true");
	}

}
