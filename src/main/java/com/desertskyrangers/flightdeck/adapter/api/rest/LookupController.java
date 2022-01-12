package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactOption;
import com.desertskyrangers.flightdeck.core.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class LookupController {

	@GetMapping( path = ApiPath.AIRCRAFT_STATUS )
	ResponseEntity<List<ReactOption>> aircraftStatus() {
		return new ResponseEntity<>( Arrays.stream( AircraftStatus.values() ).map( c -> new ReactOption( c.name().toLowerCase(), c.getName() ) ).toList(), HttpStatus.OK );
	}

	@GetMapping( path = ApiPath.AIRCRAFT_TYPE )
	ResponseEntity<List<ReactOption>> aircraftType() {
		return new ResponseEntity<>( Arrays.stream( AircraftType.values() ).map( c -> new ReactOption( c.name().toLowerCase(), c.getName() ) ).toList(), HttpStatus.OK );
	}

	@GetMapping( path = ApiPath.BATTERY_STATUS )
	ResponseEntity<List<ReactOption>> batteryStatus() {
		return new ResponseEntity<>( Arrays.stream( BatteryStatus.values() ).map( c -> new ReactOption( c.name().toLowerCase(), c.getName() ) ).toList(), HttpStatus.OK );
	}

	@GetMapping( path = ApiPath.BATTERY_TYPE )
	ResponseEntity<List<ReactOption>> batteryType() {
		return new ResponseEntity<>( Arrays.stream( BatteryType.values() ).map( c -> new ReactOption( c.name().toLowerCase(), c.getName() ) ).toList(), HttpStatus.OK );
	}

	@GetMapping( path = ApiPath.SMS_CARRIERS )
	ResponseEntity<List<ReactOption>> smsCarriers() {
		return new ResponseEntity<>( Arrays.stream( SmsCarrier.values() ).map( c -> new ReactOption( c.name().toLowerCase(), c.getName() ) ).toList(), HttpStatus.OK );
	}

}
