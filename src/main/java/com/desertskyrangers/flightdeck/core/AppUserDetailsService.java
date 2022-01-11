package com.desertskyrangers.flightdeck.core;

import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppUserDetailsService implements UserDetailsService {

	private final StateRetrieving stateRetrieving;

	public AppUserDetailsService( StateRetrieving stateRetrieving ) {
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public AppTokenDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
		return stateRetrieving.findUserTokenByPrincipal( username ).map( AppTokenDetails::new ).orElseThrow( () -> new UsernameNotFoundException( username ) );
	}

}
