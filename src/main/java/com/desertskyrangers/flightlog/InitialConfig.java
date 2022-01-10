package com.desertskyrangers.flightlog;

import com.desertskyrangers.flightlog.core.model.*;
import com.desertskyrangers.flightlog.port.StatePersisting;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;
import java.util.UUID;

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
		User user = new User();
		user.tokens( Set.of( usernameToken, emailToken ) );
		user.firstName( "Tia" );
		user.lastName( "Test" );
		user.preferredName( "Tia Test");
		user.email( "tiat@example.com" );
		user.smsNumber( "800-555-8428" );
		user.smsCarrier( SmsCarrier.SPRINT );
		statePersisting.upsert( user );

		Aircraft aftyn = new Aircraft().id( UUID.randomUUID() ).name( "AFTYN" ).type( AircraftType.FIXEDWING ).make("Hobby King").model("Bixler 2").status( AircraftStatus.DESTROYED ).owner( user.id() ).ownerType( AircraftOwnerType.USER );
		Aircraft bianca = new Aircraft().id( UUID.randomUUID() ).name( "BIANCA" ).type( AircraftType.FIXEDWING ).make("Hobby King").model("Bixler 2").status( AircraftStatus.DESTROYED ).owner( user.id() ).ownerType( AircraftOwnerType.USER );
		Aircraft gemma = new Aircraft().id( UUID.randomUUID() ).name( "GEMMA" ).type( AircraftType.MULTIROTOR ).status( AircraftStatus.AIRWORTHY ).owner( user.id() ).ownerType( AircraftOwnerType.USER );
		Aircraft helena = new Aircraft().id( UUID.randomUUID() ).name( "HELENA" ).type( AircraftType.HELICOPTER ).status( AircraftStatus.INOPERATIVE ).owner( user.id() ).ownerType( AircraftOwnerType.USER );
		statePersisting.upsert( aftyn );
		statePersisting.upsert( bianca );
		statePersisting.upsert( gemma );
		statePersisting.upsert( helena );

		stateRetrieving.findUserTokenByPrincipal( usernameToken.principal() ).ifPresent( t -> log.warn( "Tester created=" + t.principal() ) );
	}

}
