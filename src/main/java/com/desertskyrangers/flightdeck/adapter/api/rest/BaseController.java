package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseController {

	private UserService userService;

	private User unlistedUser;

	@Autowired
	protected void setUserService( UserService userService ) {
		this.userService = userService;
	}

	@Autowired
	public void setUnlistedUser( User unlistedUser ) {
		this.unlistedUser = unlistedUser;
	}

	protected User findUser( Authentication authentication ) {
		String username = authentication.getName();
		return userService.findByPrincipal( username ).orElseThrow( () -> new UsernameNotFoundException( username ) );
	}

	protected User unlistedUser() {
		return unlistedUser;
	}

}
