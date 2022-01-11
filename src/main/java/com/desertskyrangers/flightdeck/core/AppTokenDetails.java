package com.desertskyrangers.flightdeck.core;

import com.desertskyrangers.flightdeck.core.model.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AppTokenDetails implements UserDetails {

	private final UserToken token;

	public AppTokenDetails( UserToken token ) {
		this.token = token;
	}

	@Override
	public Collection<AppRole> getAuthorities() {
		// Authorities can be assigned directly (not implemented)
		// and authorities can come through the user roles.

		Set<AppRole> authorities = token.user().roles().stream().map( r -> AppRole.valueOf( r.toUpperCase() ) ).collect( Collectors.toSet() );
		authorities.add( AppRole.USER );
		return authorities;
	}

	@Override
	public String getUsername() {
		return token.principal();
	}

	@Override
	public String getPassword() {
		return token.credential();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
