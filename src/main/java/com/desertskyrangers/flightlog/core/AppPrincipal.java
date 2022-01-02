package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AppPrincipal implements UserDetails {

	private final UserToken credential;

	public AppPrincipal( UserToken credential ) {
		this.credential = credential;
	}

	@Override
	public Collection<AppRole> getAuthorities() {
		// Authorities can be assigned directly (not implemented)
		// and authorities can come through the user roles.

		Set<AppRole> authorities = credential.userAccount().roles().stream().map( r -> AppRole.valueOf( r.toUpperCase() ) ).collect( Collectors.toSet() );
		authorities.add( AppRole.USER );
		return authorities;
	}

	@Override
	public String getPassword() {
		return credential.credential();
	}

	@Override
	public String getUsername() {
		return credential.principal();
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
