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

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public InitialConfig( StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	void init() {
		UserToken usernameToken = new UserToken();
		usernameToken.principal( "tester" );
		usernameToken.credential( new BCryptPasswordEncoder().encode( "tester" ) );
		UserToken emailToken = new UserToken();
		emailToken.principal( "tester@noreply.com" );
		emailToken.credential( new BCryptPasswordEncoder().encode( "tester" ) );
		UserAccount user = new UserAccount();
		user.tokens( Set.of( usernameToken, emailToken ) );
		user.firstName( "Test" );
		user.lastName( "user" );
		statePersisting.upsert( user );

		log.warn( "User created=" + stateRetrieving.findUserTokenByPrincipal( "tester" ).get().principal() );
	}

}
