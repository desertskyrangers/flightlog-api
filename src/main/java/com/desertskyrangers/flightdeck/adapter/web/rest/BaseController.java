package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtTokenProvider;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public abstract class BaseController {

	protected static final String DEFAULT_PAGE_SIZE = "20";

	private UserServices userServices;

	private JwtTokenProvider jwtTokenProvider;

	private User unlistedUser;

	@Autowired
	protected void setUserService( UserServices userServices ) {
		this.userServices = userServices;
	}

	@Autowired
	public void setJwtTokenProvider( JwtTokenProvider jwtTokenProvider ) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Autowired
	public void setUnlistedUser( User unlistedUser ) {
		this.unlistedUser = unlistedUser;
	}

	protected User getRequester( Authentication authentication ) {
		// After going through the JwtFilter the JWT is in the authentication credentials
		String requesterId = jwtTokenProvider.getUserId( String.valueOf( authentication.getCredentials() ) );
		return userServices.find( UUID.fromString( requesterId ) ).orElseThrow( () -> new SecurityException( "Request missing user id" ) );
	}

	protected User unlistedUser() {
		return unlistedUser;
	}

}
