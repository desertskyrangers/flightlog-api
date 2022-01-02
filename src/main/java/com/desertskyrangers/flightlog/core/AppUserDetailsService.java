package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.port.StateRetrieving;
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
	public AppPrincipal loadUserByUsername( String username ) throws UsernameNotFoundException {
		return stateRetrieving.findUserTokenByPrincipal( username ).map( AppPrincipal::new ).orElseThrow( () -> new UsernameNotFoundException( username ) );
	}

}
