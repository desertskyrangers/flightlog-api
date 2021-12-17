package com.desertskyrangers.flightlog.plug.api;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.util.Json;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthController {

	private final AuthRequesting authRequesting;

	public AuthController( AuthRequesting authRequesting ) {
		this.authRequesting = authRequesting;
	}

	@PostMapping( ApiPath.SIGNUP )
	@ResponseStatus( HttpStatus.ACCEPTED )
	void signup( @RequestBody ReactUserAccount request ) {
		List<String> messages = new ArrayList<>();
		if( request.getUsername() == null ) messages.add( "Username required" );
		if( request.getPassword() == null ) messages.add( "Password required" );
		if( request.getEmail() == null ) messages.add( "EmailRequired" );
		if( !messages.isEmpty() ) throw new ResponseStatusException( HttpStatus.BAD_REQUEST, Json.stringify( messages ) );

		authRequesting.requestUserAccountSignup( new UserAccount().username( request.getUsername() ).password( request.getPassword() ).email( request.getEmail() ) );
	}

}
