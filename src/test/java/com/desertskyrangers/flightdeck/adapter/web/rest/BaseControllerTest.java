package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtTokenProvider;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.core.model.UserToken;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Objects;
import java.util.Set;

@AutoConfigureMockMvc
@WithMockUser( authorities = "USER" )
public abstract class BaseControllerTest extends BaseTest {

	private static final String HEADER_KEY = "Authorization";

	private static final String TOKEN_PREFIX = "Bearer";

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	protected User mockUser;

	protected String jwt;

	@BeforeEach
	protected void setup() {
		super.setup();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if( authentication != null ) {
			String username = authentication.getName();
			String password = Objects.toString( authentication.getCredentials() );
			String encodedPassword = passwordEncoder.encode( password );

			// Delete the exising mock user account
			userServices.findByPrincipal( authentication.getName() ).ifPresent( u -> userServices.remove( u ) );
			//if( userServices.findByPrincipal( authentication.getName() ).isPresent())

			mockUser = userServices.findByPrincipal( authentication.getName() ).orElseGet( () -> {
				User user = new User();
				user.username( username );
				user.email( username + "@example.com" );
				user.firstName( "Mock" );
				user.lastName( "User" );
				UserToken usernameToken = new UserToken().principal( user.username() ).credential( encodedPassword );
				UserToken emailToken = new UserToken().principal( user.email() ).credential( encodedPassword );
				user.tokens( Set.of( usernameToken, emailToken ) );
				usernameToken.user( user );
				emailToken.user( user );
				userServices.upsert( user );
				return user;
			} );

			stateRetrieving.findAircraftByOwner( getMockUser().id() ).forEach( statePersisting::remove );
			stateRetrieving.findBatteriesByOwner( getMockUser().id() ).forEach( statePersisting::remove );

			jwt = jwtTokenProvider.createToken( mockUser, authentication, false );
		}

	}

	protected User getMockUser() {
		return mockUser;
	}

	protected RequestPostProcessor jwt() {
		return request -> {
			request.addHeader( HEADER_KEY, TOKEN_PREFIX + " " + jwt );
			return request;
		};
	}

	protected RequestPostProcessor nojwt() {
		return request -> {
			request.removeHeader( HEADER_KEY );
			return request;
		};
	}

}

