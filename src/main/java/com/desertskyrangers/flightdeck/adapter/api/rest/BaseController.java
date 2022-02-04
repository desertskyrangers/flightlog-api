package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.jwt.JwtToken;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.UserService;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import java.util.UUID;

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

	// FIXME Doesn't work...yet
	protected User findUser( JwtToken token ) {
		Jwt<?,?> jwt = (Jwt<?,?>)Jwts.parserBuilder().build().parse( token.getToken() );
		String id = (String)jwt.getHeader().get( JwtToken.USER_ID_CLAIM_KEY );
		return userService.find( UUID.fromString( id ) ).orElseThrow( () -> new UsernameNotFoundException( id ) );
	}

	protected User unlistedUser() {
		return unlistedUser;
	}

}
