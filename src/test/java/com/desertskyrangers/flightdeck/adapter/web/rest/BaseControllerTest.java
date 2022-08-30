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

			// Create mock user account
			mockUser = new User();
			mockUser.username( username );
			mockUser.email( username + "@example.com" );
			mockUser.firstName( "Mock" );
			mockUser.lastName( "User" );
			UserToken usernameToken = new UserToken().principal( mockUser.username() ).credential( encodedPassword );
			UserToken emailToken = new UserToken().principal( mockUser.email() ).credential( encodedPassword );
			mockUser.tokens( Set.of( usernameToken, emailToken ) );
			usernameToken.user( mockUser );
			emailToken.user( mockUser );
			userServices.upsert( mockUser );

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

