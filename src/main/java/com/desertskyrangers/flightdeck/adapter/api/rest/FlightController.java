package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactFlight;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactFlightResponse;
import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.FlightService;
import com.desertskyrangers.flightdeck.util.Text;
import com.desertskyrangers.flightdeck.util.Uuid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class FlightController extends BaseController {

	private final FlightService flightService;

	public FlightController( FlightService flightService ) {
		this.flightService = flightService;
	}

	@GetMapping( path = ApiPath.FLIGHT + "/{id}" )
	ResponseEntity<ReactFlightResponse> getFlight( Authentication authentication, @PathVariable UUID id ) {
		List<String> messages = new ArrayList<>();
		try {
			User requester = findUser( authentication );
			Optional<Flight> optional = flightService.find( id );
			if( optional.isPresent() ) {
				return new ResponseEntity<>( new ReactFlightResponse().setFlight( ReactFlight.from( requester, optional.get() ) ), HttpStatus.OK );
			} else {
				messages.add( "Flight id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error getting flight", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactFlightResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PostMapping( path = ApiPath.FLIGHT )
	ResponseEntity<ReactFlightResponse> newFlight( Authentication authentication, @RequestBody ReactFlight request ) {
		// Replace 'new' with random id
		request.setId( UUID.randomUUID().toString() );
		return updateFlight( authentication, request );
	}

	@PutMapping( path = ApiPath.FLIGHT )
	ResponseEntity<ReactFlightResponse> updateFlight( Authentication authentication, @RequestBody ReactFlight request ) {
		String id = request.getId();
		String pilot = request.getPilot();
		String observer = request.getObserver();
		String aircraft = request.getAircraft();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Text.isNotBlank( id ) && Uuid.isNotValid( id ) ) messages.add( "Invalid flight id" );
		if( Text.isBlank( pilot ) ) messages.add( "Pilot required" );
		if( Text.isNotBlank( pilot ) && Uuid.isNotValid( pilot ) ) messages.add( "Invalid pilot id" );
		if( Text.isNotBlank( observer ) && Uuid.isNotValid( observer ) ) messages.add( "Invalid observer id" );
		if( Text.isBlank( aircraft ) ) messages.add( "Aircraft required" );
		if( Text.isNotBlank( aircraft ) && Uuid.isNotValid( aircraft ) ) messages.add( "Invalid aircraft id" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactFlightResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			User user = findUser( authentication );
			flightService.upsert( ReactFlight.toUpsertRequest( request ).user( user ) );
			return new ResponseEntity<>( new ReactFlightResponse().setFlight( request ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error updating flight", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactFlightResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@DeleteMapping( path = ApiPath.FLIGHT )
	ResponseEntity<ReactFlightResponse> deleteFlight(Authentication authentication, @RequestBody Map<String, Object> flight ) {
		UUID id = UUID.fromString( String.valueOf( flight.get( "id" ) ) );
		log.info( "Delete flight" );
		List<String> messages = new ArrayList<>();
		try {
			User requester = findUser( authentication );
			Optional<Flight> optional = flightService.find( id );
			if( optional.isPresent() ) {
				Flight deletedFlight = optional.get();
				flightService.remove( deletedFlight );
				return new ResponseEntity<>( new ReactFlightResponse().setFlight( ReactFlight.from( requester, deletedFlight ) ), HttpStatus.OK );
			} else {
				messages.add( "Flight id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error removing flight", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactFlightResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

}
