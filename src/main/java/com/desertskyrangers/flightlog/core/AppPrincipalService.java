package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppPrincipalService implements UserDetailsService {

	private final StateRetrieving stateRetrieving;

	public AppPrincipalService( StateRetrieving stateRetrieving ) {
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
		return stateRetrieving.findUserCredentialByUsername( username ).map( AppPrincipal::new ).orElse( null );
	}

}
