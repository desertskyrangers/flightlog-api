package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactBasicCredentials;
import com.desertskyrangers.flightlog.adapter.api.model.ReactSignupRequest;
import com.desertskyrangers.flightlog.adapter.api.model.ReactUserAccount;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredentials;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.port.UserManagement;
import com.desertskyrangers.flightlog.util.Json;
import com.desertskyrangers.flightlog.util.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class AuthController {

	private final AuthRequesting authRequesting;

	private final UserManagement userManagement;

	public AuthController( AuthRequesting authRequesting, UserManagement userManagement ) {
		this.authRequesting = authRequesting;
		this.userManagement = userManagement;
	}

	@GetMapping( path = ApiPath.AUTH_CSRF )
	CsrfToken csrf( CsrfToken token ) {
		return token;
	}

	@PostMapping( path = ApiPath.AUTH_SIGNUP, consumes = "application/json", produces = "application/json" )
	ResponseEntity<Map<String, Object>> signup( @RequestBody ReactSignupRequest request ) {
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username required" );
		if( Text.isBlank( request.getPassword() ) ) messages.add( "Password required" );
		if( Text.isBlank( request.getEmail() ) ) messages.add( "EmailRequired" );
		if( userManagement.findByUsername( request.getUsername() ).isPresent() ) messages.add( "Username unavailable" );

		// FIXME Check for invalid data
		// - Username only uses valid characters (do I need this if only for authentication?)
		// - Username is not taken
		// - Username is long enough (>4 chars)
		// - Username is not too long (~64 chars)
		// - Password is long enough (>8 chars)
		// - Password is not too long (~128 chars)
		// - Email only uses valid characters
		// - Email is in correct format
		// - Email is not taken (what if I have two accounts with the same email?)
		if( !messages.isEmpty() ) return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.BAD_REQUEST );

		try {
			UserAccount account = new UserAccount().email( request.getEmail() );
			UserCredentials credentials = new UserCredentials().userAccount( account ).username( request.getUsername() ).password( request.getPassword() );
			authRequesting.requestUserAccountSignup( account, credentials );

			ReactUserAccount response = new ReactUserAccount().setId( account.id().toString() ).setEmail( account.email() );
			return new ResponseEntity<>( Json.asMap( response ), HttpStatus.ACCEPTED );
		} catch( Exception exception ) {
			log.error( "Error during account sign up, username=" + request.getUsername(), exception );
			return new ResponseEntity<>( Map.of( "messages", List.of( "There was an error creating the account" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}

	@GetMapping( path = ApiPath.AUTH_VERIFY, produces = "application/json" )
	ResponseEntity<Map<String, Object>> verify( @RequestParam String id, @RequestParam String code ) {
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Text.isBlank( code ) ) messages.add( "Code required" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.BAD_REQUEST );

		try {
			Verification verification = new Verification().id( UUID.fromString( id ) ).code( code );
			authRequesting.requestUserVerify( verification );

			//			ReactUserAccount response = new ReactUserAccount();
			//			response.setId( account.id().toString() );
			//			response.setUsername( account.username()  );
			//			response.setEmail( account.email() );
			//			return new ResponseEntity<>( Json.asMap( response ), HttpStatus.ACCEPTED );
		} catch( Exception exception ) {
			log.error( "Error during email verification, id=" + id, exception );
			return new ResponseEntity<>( Map.of( "messages", List.of( "There was an error verifying the email address" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}

		return new ResponseEntity<>( Map.of(), HttpStatus.OK );
	}

	@PostMapping( path = ApiPath.AUTH_LOGIN, consumes = "application/json", produces = "application/json" )
	ResponseEntity<Map<String, Object>> login( @RequestBody ReactBasicCredentials request ) {
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
