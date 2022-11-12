package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactLocation;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactLocation;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactResponse;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactUser;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.core.model.LocationStatus;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.LocationServices;
import com.desertskyrangers.flightdeck.port.UserServices;
import com.desertskyrangers.flightdeck.util.Text;
import com.desertskyrangers.flightdeck.util.Uuid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class LocationController extends BaseController {

	private final LocationServices locationServices;

	private final UserServices userServices;

	public LocationController( LocationServices locationServices, UserServices userServices ) {
		this.locationServices = locationServices;
		this.userServices = userServices;
	}

	@GetMapping( path = ApiPath.LOCATION + "/{id}" )
	ResponseEntity<ReactResponse<ReactLocation>> getLocation( @PathVariable UUID id ) {
		log.info( "Get location" );
		List<String> messages = new ArrayList<>();
		try {
			Optional<Location> optional = locationServices.find( id );
			if( optional.isPresent() ) {
				return new ResponseEntity<>( ReactResponse.of( ReactLocation.from( optional.get() ) ), HttpStatus.OK );
			} else {
				messages.add( "Location id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error getting location", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PostMapping( path = ApiPath.LOCATION )
	ResponseEntity<ReactResponse<ReactLocation>> newLocation( Authentication authentication, @RequestBody ReactLocation request ) {
		String name = request.getName();
		String status = request.getStatus();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( name ) ) messages.add( "Name required" );
		//if( Text.isBlank( type ) ) messages.add( "Type required" );
		if( Text.isBlank( status ) ) messages.add( "Status required" );
		//if( LocationType.isNotValid( type ) ) messages.add( "Invalid type: " + type );
		if( LocationStatus.isNotValid( status ) ) messages.add( "Invalid status: " + status );
		if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

		try {
			request.setId( UUID.randomUUID().toString() );
			//request.setUser( ReactUser.from( getRequester( authentication ) ) );
			locationServices.upsert( ReactLocation.toLocation( request ) );
			return new ResponseEntity<>( ReactResponse.of( request ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error creating new location", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PutMapping( path = ApiPath.LOCATION )
	ResponseEntity<ReactResponse<ReactLocation>> updateLocation( Authentication authentication, @RequestBody ReactLocation request ) {
		log.info( "Update location" );
		String id = request.getId();
		String name = request.getName();
		//		String type = request.getType();
		String status = request.getStatus();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Text.isBlank( name ) ) messages.add( "Name required" );
		//		if( Text.isBlank( type ) ) messages.add( "Type required" );
		if( Text.isBlank( status ) ) messages.add( "Status required" );
		if( Uuid.isNotValid( id ) ) messages.add( "invalid ID" );
		//		if( LocationType.isNotValid( type ) ) messages.add( "Invalid type: " + type );
		if( LocationStatus.isNotValid( status ) ) messages.add( "Invalid status: " + status );
		if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

		try {
			request.setUser( ReactUser.from( getRequester( authentication ) ) );
			locationServices.upsert( ReactLocation.toLocation( request ) );
			return new ResponseEntity<>( ReactResponse.of( request ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error updating location", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@DeleteMapping( path = ApiPath.LOCATION )
	ResponseEntity<ReactResponse<ReactLocation>> deleteLocation( @RequestBody Map<String, Object> location ) {
		UUID id = UUID.fromString( String.valueOf( location.get( "id" ) ) );
		log.info( "Delete location" );
		List<String> messages = new ArrayList<>();
		try {
			Optional<Location> optional = locationServices.find( id );
			if( optional.isPresent() ) {
				Location deletedLocation = optional.get();
				locationServices.remove( deletedLocation );
				return new ResponseEntity<>( ReactResponse.of( ReactLocation.from( deletedLocation ) ), HttpStatus.OK );
			} else {
				messages.add( "Location id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error removing location", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

}
