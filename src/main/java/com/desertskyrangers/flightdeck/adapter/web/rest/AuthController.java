package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtToken;
import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtTokenProvider;
import com.desertskyrangers.flightdeck.adapter.web.model.*;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.core.model.Verification;
import com.desertskyrangers.flightdeck.port.AuthServices;
import com.desertskyrangers.flightdeck.port.UserServices;
import com.desertskyrangers.flightdeck.util.PasswordChecker;
import com.desertskyrangers.flightdeck.util.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
public class AuthController {

	private final AuthServices authServices;

	private final UserServices userServices;

	private final JwtTokenProvider tokenProvider;

	private final AuthenticationManager authenticationManager;

	private final PasswordEncoder passwordEncoder;

	public AuthController( AuthServices authServices, UserServices userServices, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder ) {
		this.authServices = authServices;
		this.userServices = userServices;
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping( path = ApiPath.AUTH_RECOVER, consumes = "application/json", produces = "application/json" )
	ResponseEntity<ReactRecoverResponse> register( @RequestBody ReactRecoverRequest request ) {
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username or email required" );

		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactRecoverResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			messages.addAll( authServices.requestUserRecover( request.getUsername() ) );
		} catch( Exception exception ) {
			log.error( "Error during account sign up, username=" + request.getUsername(), exception );
			return new ResponseEntity<>( new ReactRecoverResponse().setMessages( List.of( "There was an error recovering the account" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}

		return new ResponseEntity<>( new ReactRecoverResponse().setMessages( messages ), HttpStatus.OK );
	}

	@PostMapping( path = ApiPath.AUTH_RESET, consumes = "application/json", produces = "application/json" )
	ResponseEntity<ReactResetResponse> reset( @RequestBody ReactResetRequest request ) {
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getId() ) ) messages.add( "Reset id required" );
		if( Text.isBlank( request.getPassword() ) ) {
			messages.add( "Password required" );
		} else {
			if( PasswordChecker.isNotValid( request.getPassword() ) ) messages.add( "Invalid password" );
		}
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactResetResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			Verification verification = new Verification().id( UUID.fromString( request.getId() ) );
			messages.addAll( authServices.requestUserReset( verification, request.getPassword() ) );
			return new ResponseEntity<>( new ReactResetResponse().setMessages( messages ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error during account reset, id=" + request.getId(), exception );
			return new ResponseEntity<>( new ReactResetResponse().setMessages( List.of( "There was an error resetting the account" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}

	@PostMapping( path = ApiPath.AUTH_REGISTER, consumes = "application/json", produces = "application/json" )
	ResponseEntity<ReactRegisterResponse> register( @RequestBody ReactRegisterRequest request ) {
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username required" );
		if( Text.isBlank( request.getPassword() ) ) {
			messages.add( "Password required" );
		} else {
			if( PasswordChecker.isNotValid( request.getPassword() ) ) messages.add( "Invalid password" );
		}
		if( Text.isBlank( request.getEmail() ) ) messages.add( "Email required" );

		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactRegisterResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			UUID verificationId = UUID.randomUUID();
			messages.addAll( authServices.requestUserRegister( request.getUsername(), request.getEmail(), request.getPassword(), verificationId ) );

			if( messages.isEmpty() ) {
				// Generate the JWT token like login
				String jwt = authenticate( request.getUsername(), request.getPassword(), false );
				ReactRegisterResponse response = new ReactRegisterResponse().setId( verificationId.toString() ).setJwt( new JwtToken( jwt ) );
				return new ResponseEntity<>( response, HttpStatus.ACCEPTED );
			} else {
				return new ResponseEntity<>( new ReactRegisterResponse().setMessages( messages ), HttpStatus.FORBIDDEN );
			}
		} catch( Exception exception ) {
			log.error( "Error during account sign up, username=" + request.getUsername(), exception );
			return new ResponseEntity<>( new ReactRegisterResponse().setMessages( List.of( "There was an error creating the account" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}

	@PostMapping( path = ApiPath.AUTH_RESEND, consumes = "application/json", produces = "application/json" )
	ResponseEntity<Map<String, Object>> resend( @RequestBody Map<String, Object> request ) {
		String id = (String)request.get( "id" );

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );

		try {
			messages.addAll( authServices.requestUserVerifyResend( UUID.fromString( id ) ) );
		} catch( Exception exception ) {
			log.error( "Verification resend error, id=" + id, exception );
			return new ResponseEntity<>( Map.of( "messages", List.of( "Verification resend error" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}

		return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.OK );
	}

	@PostMapping( path = ApiPath.AUTH_VERIFY, consumes = "application/json", produces = "application/json" )
	ResponseEntity<Map<String, Object>> verify( @RequestBody Map<String, Object> request ) {
		String id = (String)request.get( "id" );
		String code = (String)request.get( "code" );

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Text.isBlank( code ) ) messages.add( "Code required" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.BAD_REQUEST );

		try {
			Verification verification = new Verification().id( UUID.fromString( id ) ).code( code );
			messages.addAll( authServices.requestUserVerify( verification ) );
			if( !messages.isEmpty() ) return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.FORBIDDEN );
		} catch( Exception exception ) {
			log.error( "Verification error, id=" + id, exception );
			return new ResponseEntity<>( Map.of( "messages", List.of( "Verification error" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}

		return new ResponseEntity<>( Map.of( "messages", messages ), HttpStatus.OK );
	}

	@PostMapping( path = ApiPath.AUTH_LOGIN, consumes = "application/json", produces = "application/json" )
	ResponseEntity<ReactLoginResponse> login( @RequestBody ReactLoginRequest request ) {
		List<String> messages = new ArrayList<>();
		if( Text.isBlank( request.getUsername() ) ) messages.add( "Username required" );
		if( Text.isBlank( request.getPassword() ) ) messages.add( "Password required" );
		if( !messages.isEmpty() ) {
			log.warn( "bad request=" + messages );
			return new ResponseEntity<>( new ReactLoginResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );
		}

		// Authenticate
		try {
			log.trace( "login request=" + request.getUsername() );
			return new ResponseEntity<>( new ReactLoginResponse().setJwt( new JwtToken( authenticate( request.getUsername(), request.getPassword(), request.isRemember() ) ) ), HttpStatus.OK );
		} catch( UsernameNotFoundException exception ) {
			log.warn( "Account not found: " + request.getUsername() );
			return new ResponseEntity<>( new ReactLoginResponse().setMessages( List.of( "Account not found" ) ), HttpStatus.UNAUTHORIZED );
		} catch( DisabledException exception ) {
			log.warn( "Account disabled: " + request.getUsername() );
			return new ResponseEntity<>( new ReactLoginResponse().setMessages( List.of( "Account disabled" ) ), HttpStatus.UNAUTHORIZED );
		} catch( LockedException exception ) {
			log.warn( "Account locked: " + request.getUsername() );
			return new ResponseEntity<>( new ReactLoginResponse().setMessages( List.of( "Account locked" ) ), HttpStatus.UNAUTHORIZED );
		} catch( BadCredentialsException exception ) {
			log.warn( "Authentication failure: " + request.getUsername() );
			return new ResponseEntity<>( new ReactLoginResponse().setMessages( List.of( "Authentication failure" ) ), HttpStatus.UNAUTHORIZED );
		} catch( AuthenticationException exception ) {
			log.warn( "Authentication error: " + request.getUsername(), exception );
			return new ResponseEntity<>( new ReactLoginResponse().setMessages( List.of( "Authentication error" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}

	@PostMapping( path = ApiPath.AUTH_LOGOUT, consumes = "application/json", produces = "application/json" )
	ResponseEntity<Object> logout( @RequestBody Map<String, Object> request ) {
		return new ResponseEntity<>( null, HttpStatus.OK );
	}

	private String authenticate( String username, String password, boolean remember ) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( username, password );
		Authentication authentication = authenticationManager.authenticate( authenticationToken );
		SecurityContextHolder.getContext().setAuthentication( authentication );

		Optional<User> optionalAccount = userServices.findByPrincipal( username );
		if( optionalAccount.isEmpty() ) return "Account not found: " + username;

		// Create JWT token and return to requester
		return tokenProvider.createToken( optionalAccount.get(), authentication, remember );
	}

}
