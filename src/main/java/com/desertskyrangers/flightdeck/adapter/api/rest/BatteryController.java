package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.port.BatteryService;
import com.desertskyrangers.flightdeck.port.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BatteryController {

	private final BatteryService batteryService;

	private final UserService userService;

	public BatteryController( BatteryService batteryService, UserService userService ) {
		this.batteryService = batteryService;
		this.userService = userService;
	}


}
