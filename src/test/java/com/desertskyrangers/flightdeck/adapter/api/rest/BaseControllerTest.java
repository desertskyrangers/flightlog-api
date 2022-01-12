package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.core.model.UserToken;
import com.desertskyrangers.flightdeck.port.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Set;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

	@Autowired
	private UserService userService;

	private User user;

	@BeforeEach
	void setup() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if( authentication != null ) {
			String username = authentication.getName();

			// Delete the exising mock user account
			userService.findByPrincipal( authentication.getName() ).ifPresent( u -> userService.remove( u ) );

			// Create mock user account
			user = new User();
			UserToken token = new UserToken().principal( username );
			user.tokens( Set.of( token ) );
			token.user( user );
			userService.upsert( user );
		}
	}

	protected User getUser() {
		return user;
	}

}
