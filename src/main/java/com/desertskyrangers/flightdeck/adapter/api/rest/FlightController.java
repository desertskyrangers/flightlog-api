package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactFlight;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactFlightResponse;
import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.port.FlightService;
import com.desertskyrangers.flightdeck.port.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class FlightController {

	private final FlightService flightService;

	private final UserService userService;

	public FlightController( FlightService flightService, UserService userService ) {
		this.flightService = flightService;
		this.userService = userService;
	}

	@GetMapping( path = ApiPath.FLIGHT + "/{id}" )
	ResponseEntity<ReactFlightResponse> getFlight( @PathVariable UUID id ) {
		log.info( "Get flight" );
		List<String> messages = new ArrayList<>();
		try {
			Optional<Flight> optional = flightService.find( id );
			if( optional.isPresent() ) {
				return new ResponseEntity<>( new ReactFlightResponse().setFlight( ReactFlight.from( optional.get() ) ), HttpStatus.OK );
			} else {
				messages.add( "Flight id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error getting flight", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactFlightResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}


}
