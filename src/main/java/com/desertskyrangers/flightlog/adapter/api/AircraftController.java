package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraft;
import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraftResponse;
import com.desertskyrangers.flightlog.core.model.*;
import com.desertskyrangers.flightlog.port.AircraftService;
import com.desertskyrangers.flightlog.port.UserService;
import com.desertskyrangers.flightlog.util.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class AircraftController {

	private final AircraftService aircraftService;

	private final UserService userService;

	public AircraftController( AircraftService aircraftService, UserService userService ) {
		this.aircraftService = aircraftService;
		this.userService = userService;
	}

	@GetMapping( path = ApiPath.AIRCRAFT + "/{id}" )
	ResponseEntity<ReactAircraftResponse> getAircraft( @PathVariable UUID id ) {
		log.info( "Get aircraft" );
		List<String> messages = new ArrayList<>();
		try {
			Optional<Aircraft> optional = aircraftService.find( id );
			if( optional.isPresent() ) {
				return new ResponseEntity<>( new ReactAircraftResponse().setAircraft( ReactAircraft.from( optional.get() ) ), HttpStatus.OK );
			} else {
				messages.add( "Aircraft id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error getting aircraft", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactAircraftResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PostMapping( path = ApiPath.AIRCRAFT )
	ResponseEntity<ReactAircraftResponse> newAircraft( Authentication authentication, @RequestBody ReactAircraft request ) {
		String name = request.getName();
		String type = request.getType();
		String status = request.getStatus();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( name ) ) messages.add( "Name required" );
		if( Text.isBlank( type ) ) messages.add( "Type required" );
		if( Text.isBlank( status ) ) messages.add( "Status required" );
		if( AircraftType.isNotValid( type ) ) messages.add( "Invalid type: " + type );
		if( AircraftStatus.isNotValid( status ) ) messages.add( "Invalid status: " + status );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactAircraftResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			String username = authentication.getName();
			User user = userService.findByPrincipal( username ).orElseThrow( () -> new UsernameNotFoundException( username ) );

			request.setId( UUID.randomUUID().toString() );

			// TODO Allow user to select a different owner
			request.setOwner( user.id().toString() );
			request.setOwnerType( AircraftOwnerType.USER.name().toLowerCase() );
			aircraftService.upsert( ReactAircraft.toAircraft( request ) );
			return new ResponseEntity<>( new ReactAircraftResponse().setAircraft( request ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error creating new aircraft", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactAircraftResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PutMapping( path = ApiPath.AIRCRAFT )
	ResponseEntity<ReactAircraftResponse> updateAircraft(  Authentication authentication, @RequestBody ReactAircraft request ) {
		log.info( "Update aircraft" );
		String id = request.getId();
		String name = request.getName();
		String type = request.getType();
		String status = request.getStatus();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Text.isBlank( name ) ) messages.add( "Name required" );
		if( Text.isBlank( type ) ) messages.add( "Type required" );
		if( Text.isBlank( status ) ) messages.add( "Status required" );
		if( AircraftType.isNotValid( type ) ) messages.add( "Invalid type: " + type );
		if( AircraftStatus.isNotValid( status ) ) messages.add( "Invalid status: " + status );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactAircraftResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			// Default to user ownership for now
			String username = authentication.getName();
			User user = userService.findByPrincipal( username ).orElseThrow( () -> new UsernameNotFoundException( username ) );

			// TODO Allow user to select a different owner
			request.setOwner( user.id().toString() );
			request.setOwnerType( AircraftOwnerType.USER.name().toLowerCase() );
			aircraftService.upsert( ReactAircraft.toAircraft( request ) );
			return new ResponseEntity<>( new ReactAircraftResponse().setAircraft( request ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error updating aircraft", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactAircraftResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@DeleteMapping( path = ApiPath.AIRCRAFT )
	ResponseEntity<ReactAircraftResponse> deleteAircraft( @RequestBody UUID id ) {
		log.info( "Delete aircraft" );
		List<String> messages = new ArrayList<>();
		try {
			Optional<Aircraft> optional = aircraftService.find( id );
			if( optional.isPresent() ) {
				Aircraft aircraft = optional.get();
				aircraftService.remove( aircraft );
				return new ResponseEntity<>( new ReactAircraftResponse().setAircraft( ReactAircraft.from( aircraft ) ), HttpStatus.OK );
			} else {
				messages.add( "Aircraft id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error removing aircraft", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactAircraftResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

}
