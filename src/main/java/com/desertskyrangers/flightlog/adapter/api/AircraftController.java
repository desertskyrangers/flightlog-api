package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraft;
import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraftPageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class AircraftController {

	@GetMapping( path = ApiPath.USER_AIRCRAFT + "/{page}" )
	ResponseEntity<ReactAircraftPageResponse> getAircraftPage( @PathVariable int page ) {
		ReactAircraft aftyn = new ReactAircraft().setId( UUID.randomUUID() ).setName( "AFTYN" );
		ReactAircraft bianca = new ReactAircraft().setId( UUID.randomUUID() ).setName( "BIANCA" );
		ReactAircraft chloe = new ReactAircraft().setId( UUID.randomUUID() ).setName( "CHLOE" );
		ReactAircraft danica = new ReactAircraft().setId( UUID.randomUUID() ).setName( "DANICA" );

		List<ReactAircraft> page0 = List.of( aftyn, bianca );
		List<ReactAircraft> page1 = List.of( bianca, chloe, danica );

		return new ResponseEntity<>( new ReactAircraftPageResponse().setAircraft( page == 0 ? page0 : page1 ), HttpStatus.OK );
	}

}
