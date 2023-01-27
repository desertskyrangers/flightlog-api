package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactAircraft;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactResponse;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.AircraftServices;
import com.desertskyrangers.flightdeck.port.UserServices;
import com.desertskyrangers.flightdeck.util.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class AircraftController {

	private final AircraftServices aircraftServices;

	private final UserServices userServices;

	public AircraftController( AircraftServices aircraftServices, UserServices userServices ) {
		this.aircraftServices = aircraftServices;
		this.userServices = userServices;
	}

	@GetMapping( path = ApiPath.AIRCRAFT + "/{id}" )
	ResponseEntity<ReactResponse<ReactAircraft>> getAircraft( @PathVariable UUID id ) {
		log.info( "Get aircraft" );
		List<String> messages = new ArrayList<>();
		try {
			Optional<Aircraft> optional = aircraftServices.find( id );
			if( optional.isPresent() ) {
				return new ResponseEntity<>( ReactResponse.of( ReactAircraft.from( optional.get() ) ), HttpStatus.OK );
			} else {
				messages.add( "Aircraft id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error getting aircraft", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PostMapping( path = ApiPath.AIRCRAFT )
	ResponseEntity<ReactResponse<ReactAircraft>> newAircraft( Authentication authentication, @RequestBody ReactAircraft request ) {
		String name = request.getName();
		String type = request.getType();
		String status = request.getStatus();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( name ) ) messages.add( "Name required" );
		if( Text.isBlank( type ) ) messages.add( "Type required" );
		if( Text.isBlank( status ) ) messages.add( "Status required" );
		if( AircraftType.isNotValid( type ) ) messages.add( "Invalid type: " + type );
		if( Aircraft.Status.isNotValid( status ) ) messages.add( "Invalid status: " + status );
		if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

		try {
			String username = authentication.getName();
			User user = userServices.findByPrincipal( username ).orElseThrow( () -> new UsernameNotFoundException( username ) );

			request.setId( UUID.randomUUID().toString() );

			// TODO Allow user to select a different owner
			request.setOwner( user.id().toString() );
			request.setOwnerType( OwnerType.USER.name().toLowerCase() );
			aircraftServices.upsert( ReactAircraft.toAircraft( request ) );
			return new ResponseEntity<>( ReactResponse.of( request ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error creating new aircraft", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PutMapping( path = ApiPath.AIRCRAFT )
	ResponseEntity<ReactResponse<ReactAircraft>> updateAircraft( Authentication authentication, @RequestBody ReactAircraft request ) {
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
		if( Aircraft.Status.isNotValid( status ) ) messages.add( "Invalid status: " + status );
		if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

		try {
			// Default to user ownership for now
			String username = authentication.getName();
			User user = userServices.findByPrincipal( username ).orElseThrow( () -> new UsernameNotFoundException( username ) );

			// TODO Allow user to select a different owner
			request.setOwner( user.id().toString() );
			request.setOwnerType( OwnerType.USER.name().toLowerCase() );
			aircraftServices.upsert( ReactAircraft.toAircraft( request ) );
			return new ResponseEntity<>( ReactResponse.of( request ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error updating aircraft", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@DeleteMapping( path = ApiPath.AIRCRAFT )
	ResponseEntity<ReactResponse<ReactAircraft>> deleteAircraft( @RequestBody Map<String, Object> aircraft ) {
		UUID id = UUID.fromString( String.valueOf( aircraft.get( "id" ) ) );
		log.info( "Delete aircraft" );
		List<String> messages = new ArrayList<>();
		try {
			Optional<Aircraft> optional = aircraftServices.find( id );
			if( optional.isPresent() ) {
				Aircraft deletedAircraft = optional.get();
				aircraftServices.remove( deletedAircraft );
				return new ResponseEntity<>( ReactResponse.of( ReactAircraft.from( deletedAircraft ) ), HttpStatus.OK );
			} else {
				messages.add( "Aircraft id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error removing aircraft", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

}
