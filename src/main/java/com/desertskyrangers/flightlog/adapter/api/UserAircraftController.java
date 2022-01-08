package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraft;
import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraftPageResponse;
import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraftResponse;
import com.desertskyrangers.flightlog.core.AircraftService;
import com.desertskyrangers.flightlog.core.UserService;
import com.desertskyrangers.flightlog.core.model.AircraftOwnerType;
import com.desertskyrangers.flightlog.core.model.AircraftStatus;
import com.desertskyrangers.flightlog.core.model.AircraftType;
import com.desertskyrangers.flightlog.core.model.User;
import com.desertskyrangers.flightlog.util.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class UserAircraftController {

	private final AircraftService aircraftService;

	private final UserService userService;

	public UserAircraftController( AircraftService aircraftService, UserService userService ) {
		this.aircraftService = aircraftService;
		this.userService = userService;
	}

	@GetMapping( path = ApiPath.USER_AIRCRAFT + "/{page}" )
	ResponseEntity<ReactAircraftPageResponse> getAircraftPage( @PathVariable int page ) {
		ReactAircraft aftyn = new ReactAircraft().setId( "0" ).setName( "AFTYN" );
		ReactAircraft bianca = new ReactAircraft().setId( "1" ).setName( "BIANCA" );
		ReactAircraft chloe = new ReactAircraft().setId( "2" ).setName( "CHLOE" );
		ReactAircraft danica = new ReactAircraft().setId( "3" ).setName( "DANICA" );

		List<ReactAircraft> page0 = List.of( aftyn, bianca );
		List<ReactAircraft> page1 = List.of( bianca, chloe, danica );

		return new ResponseEntity<>( new ReactAircraftPageResponse().setAircraft( page == 0 ? page0 : page1 ), HttpStatus.OK );
	}

	@PostMapping( path = ApiPath.USER_AIRCRAFT )
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
			request.setOwner( user.id().toString() );
			request.setOwnerType( AircraftOwnerType.USER.name().toLowerCase() );
			aircraftService.upsert( ReactAircraft.toAircraft( request ) );
		} catch( Exception exception ) {
			log.error( "Error creating new aircraft", exception );
			return new ResponseEntity<>( new ReactAircraftResponse().setMessages( List.of( exception.getMessage() ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}

		return new ResponseEntity<>( new ReactAircraftResponse().setAircraft( request ), HttpStatus.OK );
	}

	@PutMapping( path = ApiPath.USER_AIRCRAFT )
	ResponseEntity<ReactAircraftResponse> updateAircraft( @RequestBody ReactAircraft aircraft ) {
		log.info( "Update aircraft" );
		String id = aircraft.getId();
		String name = aircraft.getName();
		String type = aircraft.getType();
		String status = aircraft.getStatus();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Text.isBlank( name ) ) messages.add( "Name required" );
		if( Text.isBlank( type ) ) messages.add( "Type required" );
		if( Text.isBlank( status ) ) messages.add( "Status required" );
		if( AircraftType.isNotValid( type ) ) messages.add( "Invalid type: " + type );
		if( AircraftStatus.isNotValid( status ) ) messages.add( "Invalid status: " + status );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactAircraftResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		return new ResponseEntity<>( new ReactAircraftResponse().setAircraft( aircraft ), HttpStatus.OK );
	}

	@DeleteMapping( path = ApiPath.USER_AIRCRAFT )
	ResponseEntity<ReactAircraftResponse> deleteAircraft( @RequestBody UUID id ) {
		log.info( "New aircraft" );
		return new ResponseEntity<>( new ReactAircraftResponse(), HttpStatus.OK );
	}

}
