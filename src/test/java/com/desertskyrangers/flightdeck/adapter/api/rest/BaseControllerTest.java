package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.core.model.UserToken;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Objects;
import java.util.Set;

@WithMockUser
@AutoConfigureMockMvc
public abstract class BaseControllerTest extends BaseTest {

	protected User mockUser;

	@BeforeEach
	protected void setup() {
		super.setup();


		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if( authentication != null ) {
			String username = authentication.getName();
			String password = Objects.toString( authentication.getCredentials() );
			String encodedPassword = passwordEncoder.encode( password );

			// Delete the exising mock user account
			userService.findByPrincipal( authentication.getName() ).ifPresent( u -> userService.remove( u ) );

			// Create mock user account
			mockUser = new User();
			mockUser.username( username );
			mockUser.email( username + "@example.com" );
			UserToken usernameToken = new UserToken().principal( mockUser.username() ).credential( encodedPassword );
			UserToken emailToken = new UserToken().principal( mockUser.email() ).credential( encodedPassword );
			mockUser.tokens( Set.of( usernameToken, emailToken ) );
			usernameToken.user( mockUser );
			emailToken.user( mockUser );
			userService.upsert( mockUser );
		}

	}

	protected User getMockUser() {
		return mockUser;
	}

}
