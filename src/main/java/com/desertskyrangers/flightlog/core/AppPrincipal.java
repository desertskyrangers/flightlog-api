package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserCredential;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AppPrincipal implements UserDetails {

	private final UserCredential credential;

	public AppPrincipal( UserCredential credential ) {
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
		return credential.password();
	}

	@Override
	public String getUsername() {
		return credential.username();
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
