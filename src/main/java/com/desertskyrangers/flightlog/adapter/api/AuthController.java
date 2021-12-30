package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.jwt.JwtToken;
import com.desertskyrangers.flightlog.adapter.api.jwt.JwtTokenProvider;
import com.desertskyrangers.flightlog.adapter.api.model.ReactBasicCredentials;
import com.desertskyrangers.flightlog.adapter.api.model.ReactRegisterRequest;
import com.desertskyrangers.flightlog.adapter.api.model.ReactRegisterResponse;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.port.UserManagement;
import com.desertskyrangers.flightlog.util.Json;
import com.desertskyrangers.flightlog.util.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

	private final JwtTokenProvider tokenProvider;

	private final AuthenticationManager authenticationManager;

	public AuthController( AuthRequesting authRequesting, UserManagement userManagement, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager ) {
		this.authRequesting = authRequesting;
		this.userManagement = userManagement;
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
	}

	@GetMapping( path = ApiPath.PROFILE )
	Map<String, Object> profile() {
		String username = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		return Map.of( "username", username );
	}

	@PostMapping( path = ApiPath.AUTH_REGISTER, consumes = "application/json", produces = "application/json" )
	ResponseEntity<Map<String, Object>> register( @RequestBody ReactRegisterRequest request ) {
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
			UserCredential credentials = new UserCredential().userAccount( account ).username( request.getUsername() ).password( request.getPassword() );
			Verification verification = new Verification();

			messages.addAll( authRequesting.requestUserAccountRegister( account, credentials, verification ) );
			if( messages.isEmpty() ) {
				ReactRegisterResponse response = new ReactRegisterResponse().setId( verification.id().toString() );
				return new ResponseEntity<>( Json.asMap( response ), HttpStatus.ACCEPTED );
			} else {
				return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.FORBIDDEN );
			}
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
			messages.addAll( authRequesting.requestUserVerify( verification ) );
			if( !messages.isEmpty() ) return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.FORBIDDEN );
		} catch( Exception exception ) {
			log.error( "Verification error, id=" + id, exception );
			return new ResponseEntity<>( Map.of( "messages", List.of( "Verification error" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}

		return new ResponseEntity<>( Map.of(), HttpStatus.OK );
	}

	@PostMapping( path = ApiPath.AUTH_LOGIN, consumes = "application/json", produces = "application/json" )
	ResponseEntity<Map<String, Object>> login( @RequestBody ReactBasicCredentials request ) {
		log.info( "login request username=" + request.getUsername() );
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username required" );
		if( Text.isBlank( request.getPassword() ) ) messages.add( "Password required" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.BAD_REQUEST );

		// Authenticate
		UserCredential credential = new UserCredential().username( request.getUsername() ).password( request.getPassword() );
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( credential.username(), credential.password() );
		try {
			Authentication authentication = this.authenticationManager.authenticate( authenticationToken );
			SecurityContextHolder.getContext().setAuthentication( authentication );

			// Create JWT token and return to requester
			String jwt = tokenProvider.createToken( authentication, request.isRemember() );
			return new ResponseEntity<>( Json.asMap( new JwtToken( jwt ) ), HttpStatus.OK );
		} catch( UsernameNotFoundException | BadCredentialsException exception ) {
			log.warn( "Authentication failure: " + request.getUsername() );
			return new ResponseEntity<>( Map.of( "messages", List.of( "Authentication failure") ), HttpStatus.UNAUTHORIZED );
		} catch( AuthenticationException exception ) {
			log.warn( "Authentication failure: " + request.getUsername(), exception );
			return new ResponseEntity<>( Map.of( "messages", List.of( "Authentication failure") ), HttpStatus.UNAUTHORIZED );
		}
	}

}
