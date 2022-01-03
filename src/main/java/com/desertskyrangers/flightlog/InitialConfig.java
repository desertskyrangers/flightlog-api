package com.desertskyrangers.flightlog;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserToken;
import com.desertskyrangers.flightlog.port.StatePersisting;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
@Slf4j
public class InitialConfig {

	private final FlightLogApp app;

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public InitialConfig( FlightLogApp app, StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.app = app;
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	void init() {
		if( app.isProduction() ) return;
		UserToken usernameToken = new UserToken();
		usernameToken.principal( "tia" );
		usernameToken.credential( new BCryptPasswordEncoder().encode( "tester" ) );
		UserToken emailToken = new UserToken();
		emailToken.principal( "tia@noreply.com" );
		emailToken.credential( new BCryptPasswordEncoder().encode( "tester" ) );
		UserAccount user = new UserAccount();
		user.tokens( Set.of( usernameToken, emailToken ) );
		user.firstName( "Tia" );
		user.lastName( "Test" );
		statePersisting.upsert( user );

		stateRetrieving.findUserTokenByPrincipal( usernameToken.principal() ).ifPresent( t -> log.warn( "Tester created=" + t.principal() ) );
	}

}
