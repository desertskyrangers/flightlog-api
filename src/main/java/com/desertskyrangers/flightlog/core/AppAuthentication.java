package com.desertskyrangers.flightlog.core;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AppAuthentication implements Authentication {

	private final AppPrincipal principal;

	public AppAuthentication(AppPrincipal principal ) {
		this.principal = principal;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return principal.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		return principal.getPassword();
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return principal.getUsername();
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated( boolean isAuthenticated ) throws IllegalArgumentException {}

	@Override
	public String getName() {
		return principal.getUsername();
	}
}
