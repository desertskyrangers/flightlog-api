package com.desertskyrangers.flightlog.plug.api;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.util.Text;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

	private final AuthRequesting authRequesting;

	public AuthController( AuthRequesting authRequesting ) {
		this.authRequesting = authRequesting;
	}

	@PostMapping( path = ApiPath.AUTH_SIGNUP, consumes = "application/json", produces = "application/json" )
	@CrossOrigin( origins = "http://localhost:3000" )
	ResponseEntity<Map<String, Object>> signup( @RequestBody ReactUserAccount request ) {
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username required" );
		if( Text.isBlank( request.getPassword() ) ) messages.add( "Password required" );
		if( Text.isBlank( request.getEmail() ) ) messages.add( "EmailRequired" );
		if( !messages.isEmpty() ) {
			return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.BAD_REQUEST );
			//throw new ResponseStatusException( HttpStatus.BAD_REQUEST, Json.stringify( messages ) );
		}
		authRequesting.requestUserAccountSignup( new UserAccount().username( request.getUsername() ).password( request.getPassword() ).email( request.getEmail() ) );
		return new ResponseEntity<>( Map.of(), HttpStatus.ACCEPTED );
	}

	@PostMapping( path = ApiPath.AUTH_LOGIN, consumes = "application/json", produces = "application/json" )
	@CrossOrigin( origins = "http://localhost:3000" )
	ResponseEntity<Map<String, Object>> login( @RequestBody ReactUserAccount request ) {
		System.out.println( "login request username=" + request.getUsername() );
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username required" );
		if( Text.isBlank( request.getPassword() ) ) messages.add( "Password required" );
		if( !messages.isEmpty() ) {
			return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.BAD_REQUEST );
		}
		//authRequesting.requestUserAccountLogin( new UserAccount().username( request.getUsername() ).password( request.getPassword() ) );
		//return new ResponseEntity<>( Map.of(), HttpStatus.OK );

		return new ResponseEntity<>( Map.of( "messages", List.of("Login not implemented") ), HttpStatus.SERVICE_UNAVAILABLE );
	}

}
