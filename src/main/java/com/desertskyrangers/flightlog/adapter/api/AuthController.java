package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactUserAccount;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.util.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class AuthController {

	private final AuthRequesting authRequesting;

	public AuthController( AuthRequesting authRequesting ) {
		this.authRequesting = authRequesting;
	}

	@GetMapping( path = ApiPath.AUTH_CSRF )
	CsrfToken csrf( CsrfToken token ) {
		return token;
	}

	//@Secured( "USER" )
	@PostMapping( path = ApiPath.AUTH_SIGNUP, consumes = "application/json", produces = "application/json" )
	ResponseEntity<Map<String, Object>> signup( @RequestBody ReactUserAccount request ) {
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username required" );
		if( Text.isBlank( request.getPassword() ) ) messages.add( "Password required" );
		if( Text.isBlank( request.getEmail() ) ) messages.add( "EmailRequired" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.BAD_REQUEST );

		try {
			authRequesting.requestUserAccountSignup( new UserAccount().username( request.getUsername() ).password( request.getPassword() ).email( request.getEmail() ) );
		} catch( Exception exception ) {
			log.error( "Error during account sign up, username=" + request.getUsername(), exception );
			return new ResponseEntity<>( Map.of( "messages", List.of( "There was an error creating the account" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}

		return new ResponseEntity<>( Map.of(), HttpStatus.ACCEPTED );
	}

	//@Secured( "USER" )
	@PostMapping( path = ApiPath.AUTH_LOGIN, consumes = "application/json", produces = "application/json" )
	ResponseEntity<Map<String, Object>> login( @RequestBody ReactUserAccount request ) {
		log.info( "login request username=" + request.getUsername() );
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username required" );
		if( Text.isBlank( request.getPassword() ) ) messages.add( "Password required" );
		if( !messages.isEmpty() ) {
			return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.BAD_REQUEST );
		}
		//authRequesting.requestUserAccountLogin( new UserAccount().username( request.getUsername() ).password( request.getPassword() ) );
		//return new ResponseEntity<>( Map.of(), HttpStatus.OK );

		return new ResponseEntity<>( Map.of( "messages", List.of( "Login not implemented" ) ), HttpStatus.SERVICE_UNAVAILABLE );
	}

}
