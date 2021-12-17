package com.desertskyrangers.flightlog.plug.api;

import com.desertskyrangers.flightlog.util.Json;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( "/api/auth" )
public class AuthController {

	@PostMapping( "/signup" )
	@ResponseStatus( HttpStatus.ACCEPTED )
	void signup( @RequestBody ReactUserAccount request ) {
		List<String> messages = new ArrayList<>();
		if( request.getUsername() == null ) messages.add( "Username required" );
		if( request.getPassword() == null ) messages.add( "Password required" );
		if( request.getEmail() == null ) messages.add( "EmailRequired" );
		if( !messages.isEmpty() ) throw new ResponseStatusException( HttpStatus.BAD_REQUEST, Json.stringify( messages ) );

		// TODO Submit the request to create a user account
	}

}
