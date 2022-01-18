package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.*;
import com.desertskyrangers.flightdeck.core.model.Aircraft;
import com.desertskyrangers.flightdeck.core.model.Battery;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.AircraftService;
import com.desertskyrangers.flightdeck.port.BatteryService;
import com.desertskyrangers.flightdeck.port.FlightService;
import com.desertskyrangers.flightdeck.port.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class UserController extends BaseController {

	private final AircraftService aircraftService;

	private final BatteryService batteryService;

	private final FlightService flightService;

	private final UserService userService;

	public UserController( AircraftService aircraftService, BatteryService batteryService, FlightService flightService, UserService userService ) {
		this.aircraftService = aircraftService;
		this.batteryService = batteryService;
		this.flightService = flightService;
		this.userService = userService;
	}

	@GetMapping( path = ApiPath.PROFILE )
	ResponseEntity<ReactProfileResponse> profile() {
		String username = ((org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

		Optional<ReactUserAccount> optional = userService.findByPrincipal( username ).map( ReactUserAccount::from );
		if( optional.isEmpty() ) return new ResponseEntity<>( new ReactProfileResponse().setAccount( new ReactUserAccount() ), HttpStatus.BAD_REQUEST );

		return new ResponseEntity<>( new ReactProfileResponse().setAccount( optional.get() ), HttpStatus.OK );
	}

	//	@GetMapping
	//	public List<ReactRegisterRequest> findAll() {
	//		return service.find().stream().map( ReactRegisterRequest::from ).collect( Collectors.toList() );
	//	}

	@GetMapping( value = ApiPath.USER + "/{id}" )
	ResponseEntity<ReactProfileResponse> findById( @PathVariable( "id" ) UUID id ) {
		Optional<ReactUserAccount> optional = userService.find( id ).map( ReactUserAccount::from );
		if( optional.isEmpty() ) return new ResponseEntity<>( new ReactProfileResponse().setAccount( new ReactUserAccount() ), HttpStatus.BAD_REQUEST );

		return new ResponseEntity<>( new ReactProfileResponse().setAccount( optional.get() ), HttpStatus.OK );
	}

	//	@PostMapping
	//	@ResponseStatus( HttpStatus.CREATED )
	//	public UUID create( @RequestBody ReactRegisterRequest resource ) {
	//		//		Preconditions.checkNotNull(resource);
	//		//		return service.create(resource);
	//		return UUID.randomUUID();
	//	}

	@PutMapping( value = ApiPath.USER + "/{id}" )
	@ResponseStatus( HttpStatus.OK )
	public ResponseEntity<ReactProfileResponse> update( @PathVariable( "id" ) UUID id, @RequestBody ReactUserAccount account ) {
		Optional<User> optional = userService.find( id );
		if( optional.isEmpty() ) return new ResponseEntity<>( new ReactProfileResponse().setAccount( new ReactUserAccount() ), HttpStatus.BAD_REQUEST );

		User user = account.update( optional.get() );

		// Update the user account
		userService.upsert( user );

		return new ResponseEntity<>( new ReactProfileResponse().setAccount( ReactUserAccount.from( user ) ), HttpStatus.OK );
	}

	@DeleteMapping( value = ApiPath.USER + "/{id}" )
	@ResponseStatus( HttpStatus.OK )
	public void delete( @PathVariable( "id" ) UUID id ) {
		userService.find( id ).ifPresent( userService::remove );
	}

	@GetMapping( path = ApiPath.USER_AIRCRAFT + "/{page}" )
	ResponseEntity<ReactAircraftPageResponse> getAircraftPage( Authentication authentication, @PathVariable int page ) {
		List<String> messages = new ArrayList<>();

		try {
			User user = findUser( authentication );
			List<ReactAircraft> aircraftPage = aircraftService.findByOwner( user.id() ).stream().map( ReactAircraft::from ).toList();
			return new ResponseEntity<>( new ReactAircraftPageResponse().setAircraft( aircraftPage ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error creating new aircraft", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactAircraftPageResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@GetMapping( path = ApiPath.USER_BATTERY + "/{page}" )
	ResponseEntity<ReactBatteryPageResponse> getBatteryPage( Authentication authentication, @PathVariable int page ) {
		List<String> messages = new ArrayList<>();

		try {
			User user = findUser( authentication );
			List<ReactBattery> batteryPage = batteryService.findByOwner( user.id() ).stream().map( ReactBattery::from ).toList();
			return new ResponseEntity<>( new ReactBatteryPageResponse().setBatteries( batteryPage ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error creating new battery", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactBatteryPageResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@GetMapping( path = ApiPath.USER_FLIGHT + "/{page}" )
	ResponseEntity<ReactFlightPageResponse> getFlightPage( Authentication authentication, @PathVariable int page ) {
		List<String> messages = new ArrayList<>();

		try {
			User user = findUser( authentication );
			List<ReactFlight> flightPage = flightService.findByPilot( user.id() ).stream().map( ReactFlight::from ).toList();
			return new ResponseEntity<>( new ReactFlightPageResponse().setFlights( flightPage ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error creating new battery", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactFlightPageResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@GetMapping( path = ApiPath.USER_AIRCRAFT_LOOKUP )
	ResponseEntity<List<ReactOption>> getAircraftLookup( Authentication authentication ) {
		User user = findUser( authentication );
		List<Aircraft> objects = aircraftService.findByOwner( user.id() );
		return new ResponseEntity<>( objects.stream().map( c -> new ReactOption( c.id().toString(), c.name() ) ).toList(), HttpStatus.OK );
	}

	@GetMapping( path = ApiPath.USER_BATTERY_LOOKUP )
	ResponseEntity<List<ReactOption>> getBatteryLookup( Authentication authentication ) {
		User user = findUser( authentication );
		List<Battery> objects = batteryService.findByOwner( user.id() );
		List<ReactOption> options = new ArrayList<>( objects.stream().map( c -> new ReactOption( c.id().toString(), c.name() ) ).toList() );
		options.add( new ReactOption( "", "No battery specified" ) );
		return new ResponseEntity<>( options, HttpStatus.OK );
	}

	@GetMapping( path = ApiPath.USER_OBSERVER_LOOKUP )
	ResponseEntity<List<ReactOption>> getObserverLookup( Authentication authentication ) {
		User user = findUser( authentication );
		// TODO Add users from associated orgs
		List<User> objects = List.of( user, unlistedUser() );
		return new ResponseEntity<>( objects.stream().map( c -> new ReactOption( c.id().toString(), c.preferredName() ) ).toList(), HttpStatus.OK );
	}

	@GetMapping( path = ApiPath.USER_PILOT_LOOKUP )
	ResponseEntity<List<ReactOption>> getPilotLookup( Authentication authentication ) {
		User user = findUser( authentication );
		// TODO Add users from associated orgs
		List<User> objects = List.of( user, unlistedUser() );
		return new ResponseEntity<>( objects.stream().map( c -> new ReactOption( c.id().toString(), c.preferredName() ) ).toList(), HttpStatus.OK );
	}

}
