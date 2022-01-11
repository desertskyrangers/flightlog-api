package com.desertskyrangers.flightdeck.core;

import org.springframework.security.core.GrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

// This is really a shortcut where roles are also authorities
// May need to expand the available authorities at some point and assign them to roles
public enum AppRole implements GrantedAuthority {

	USER( "User", "Normal user" ),
	ADMIN( "Admin", "Administrator", USER ),
	SUPER( "Super", "Super User (DSR Only)", ADMIN );

	// Role name
	private String name;

	// Role description
	private String description;

	// Other roles this role has assumed, not including itself
	private final Set<AppRole> assumed;

	AppRole( String name, String description, AppRole... assumed ) {
		this.name = name;
		this.description = description;
		this.assumed = new HashSet<>();
		addRoles( Set.of( assumed ) );
	}

	@Override
	public String getAuthority() {
		return name();
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public List<AppRole> getAuthorities() {
		List<AppRole> all = new ArrayList<>( assumed );
		all.add( this );
		all.sort( null );
		Collections.reverse( all );
		return all;
	}

	public Map<String, Object> asMap() {
		return Map.of( "id", name(), "name", getName(), "description", getDescription() );
	}

	public List<String> getAuthoritiesAsStrings() {
		return getAuthorities().stream().map( AppRole::name ).collect( Collectors.toList() );
	}

	private void addRoles( Set<AppRole> roles ) {
		roles.forEach( ( role ) -> addRoles( role.assumed ) );
		assumed.addAll( roles );
	}

}
