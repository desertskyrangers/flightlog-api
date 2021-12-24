package com.desertskyrangers.flightlog.plug.api;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.util.Json;
import com.desertskyrangers.flightlog.util.Text;
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

	@PostMapping( path = ApiPath.AUTH_SIGNUP, consumes = "application/json", produces = "application/json" )
	@ResponseStatus( HttpStatus.ACCEPTED )
	@CrossOrigin( origins = "http://localhost:3000" )
	void signup( @RequestBody ReactUserAccount request ) {
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username required" );
		if( Text.isBlank( request.getPassword() ) ) messages.add( "Password required" );
		if( Text.isBlank( request.getEmail() ) ) messages.add( "EmailRequired" );
		if( !messages.isEmpty() ) throw new ResponseStatusException( HttpStatus.BAD_REQUEST, Json.stringify( messages ) );
		authRequesting.requestUserAccountSignup( new UserAccount().username( request.getUsername() ).password( request.getPassword() ).email( request.getEmail() ) );
	}

	@PostMapping( path = ApiPath.AUTH_LOGIN, consumes = "application/json", produces = "application/json" )
	@ResponseStatus( HttpStatus.OK )
	@CrossOrigin( origins = "http://localhost:3000" )
	void login( @RequestBody ReactUserAccount request ) {
		System.out.println( "login request username=" + request.getUsername() );
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username required" );
		if( Text.isBlank( request.getPassword() ) ) messages.add( "Password required" );
		if( !messages.isEmpty() ) throw new ResponseStatusException( HttpStatus.BAD_REQUEST, Json.stringify( messages ) );
		//authRequesting.requestUserAccountSignup( new UserAccount().username( request.getUsername() ).password( request.getPassword() ).email( request.getEmail() ) );
	}

}
