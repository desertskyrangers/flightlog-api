package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactBattery;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactBatteryResponse;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.BatteryServices;
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
public class BatteryController {

	private final BatteryServices batteryServices;

	private final UserServices userServices;

	public BatteryController( BatteryServices batteryServices, UserServices userServices ) {
		this.batteryServices = batteryServices;
		this.userServices = userServices;
	}

	@GetMapping( path = ApiPath.BATTERY + "/{id}" )
	ResponseEntity<ReactBatteryResponse> getBattery( @PathVariable UUID id ) {
		log.info( "Get battery" );
		List<String> messages = new ArrayList<>();
		try {
			Optional<Battery> optional = batteryServices.find( id );
			if( optional.isPresent() ) {
				return new ResponseEntity<>( new ReactBatteryResponse().setBattery( ReactBattery.from( optional.get() ) ), HttpStatus.OK );
			} else {
				messages.add( "Battery id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error getting battery", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactBatteryResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PostMapping( path = ApiPath.BATTERY )
	ResponseEntity<ReactBatteryResponse> newBattery( Authentication authentication, @RequestBody ReactBattery request ) {
		String name = request.getName();
		String type = request.getType();
		String status = request.getStatus();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( name ) ) messages.add( "Name required" );
		if( Text.isBlank( type ) ) messages.add( "Type required" );
		if( Text.isBlank( status ) ) messages.add( "Status required" );
		if( BatteryType.isNotValid( type ) ) messages.add( "Invalid type: " + type );
		if( BatteryStatus.isNotValid( status ) ) messages.add( "Invalid status: " + status );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactBatteryResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			String username = authentication.getName();
			User user = userServices.findByPrincipal( username ).orElseThrow( () -> new UsernameNotFoundException( username ) );

			request.setId( UUID.randomUUID().toString() );

			// TODO Allow user to select a different owner
			request.setOwner( user.id().toString() );
			request.setOwnerType( OwnerType.USER.name().toLowerCase() );
			batteryServices.upsert( ReactBattery.toBattery( request ) );
			return new ResponseEntity<>( new ReactBatteryResponse().setBattery( request ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error creating new battery", exception );
			messages.add( "Unexpected error creating new battery. Please contact support." );
		}

		return new ResponseEntity<>( new ReactBatteryResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PutMapping( path = ApiPath.BATTERY )
	ResponseEntity<ReactBatteryResponse> updateBattery( Authentication authentication, @RequestBody ReactBattery request ) {
		log.info( "Update battery" );
		String id = request.getId();
		String name = request.getName();
		String type = request.getType();
		String status = request.getStatus();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Text.isBlank( name ) ) messages.add( "Name required" );
		if( Text.isBlank( type ) ) messages.add( "Type required" );
		if( Text.isBlank( status ) ) messages.add( "Status required" );
		if( BatteryType.isNotValid( type ) ) messages.add( "Invalid type: " + type );
		if( BatteryStatus.isNotValid( status ) ) messages.add( "Invalid status: " + status );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactBatteryResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			// Default to user ownership for now
			String username = authentication.getName();
			User user = userServices.findByPrincipal( username ).orElseThrow( () -> new UsernameNotFoundException( username ) );

			// TODO Allow user to select a different owner
			request.setOwner( user.id().toString() );
			request.setOwnerType( OwnerType.USER.name().toLowerCase() );
			batteryServices.upsert( ReactBattery.toBattery( request ) );
			return new ResponseEntity<>( new ReactBatteryResponse().setBattery( request ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error updating battery", exception );
			messages.add( "Unexpected error updating battery. Please contact support." );
		}

		return new ResponseEntity<>( new ReactBatteryResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@DeleteMapping( path = ApiPath.BATTERY )
	ResponseEntity<ReactBatteryResponse> deleteBattery( @RequestBody Map<String, Object> battery ) {
		UUID id = UUID.fromString( String.valueOf( battery.get( "id" ) ) );
		log.info( "Delete battery" );
		List<String> messages = new ArrayList<>();
		try {
			Optional<Battery> optional = batteryServices.find( id );
			if( optional.isPresent() ) {
				Battery deletedBattery = optional.get();
				batteryServices.remove( deletedBattery );
				return new ResponseEntity<>( new ReactBatteryResponse().setBattery( ReactBattery.from( deletedBattery ) ), HttpStatus.OK );
			} else {
				messages.add( "Battery id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error removing battery", exception );
			messages.add( "Unexpected error removing battery. Please contact support." );
		}

		return new ResponseEntity<>( new ReactBatteryResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

}
