package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraft;
import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraftPageResponse;
import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraftResponse;
import com.desertskyrangers.flightlog.util.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
public class UserAircraftController {

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
	ResponseEntity<ReactAircraftResponse> newAircraft( @RequestBody ReactAircraft aircraft ) {
		log.info( "New aircraft");

		String name = aircraft.getName();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( name ) ) messages.add( "Name required" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactAircraftResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		//aircraftManager

		return new ResponseEntity<>( new ReactAircraftResponse().setAircraft( aircraft ), HttpStatus.OK );
	}

	@PutMapping( path = ApiPath.USER_AIRCRAFT )
	ResponseEntity<ReactAircraftResponse> updateAircraft( @RequestBody ReactAircraft aircraft ) {
		log.info( "Update aircraft");
		return new ResponseEntity<>( new ReactAircraftResponse().setAircraft( aircraft ), HttpStatus.OK );
	}

	@DeleteMapping( path = ApiPath.USER_AIRCRAFT )
	ResponseEntity<ReactAircraftResponse> deleteAircraft( @RequestBody UUID id ) {
		log.info( "New aircraft");
		return new ResponseEntity<>( new ReactAircraftResponse(), HttpStatus.OK );
	}

}
