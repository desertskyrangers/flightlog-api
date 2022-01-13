package com.desertskyrangers.flightdeck;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
@Slf4j
public class InitialConfig {

	private final FlightDeckApp app;

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public InitialConfig( FlightDeckApp app, StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
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
		user.preferredName( "Tia Test" );
		user.email( "tiat@example.com" );
		user.smsNumber( "800-555-8428" );
		user.smsCarrier( SmsCarrier.SPRINT );
		statePersisting.upsert( user );

		Aircraft aftyn = new Aircraft()
			.name( "AFTYN" )
			.type( AircraftType.FIXEDWING )
			.make( "Hobby King" )
			.model( "Bixler 2" )
			.status( AircraftStatus.DESTROYED )
			.owner( user.id() )
			.ownerType( OwnerType.USER );
		Aircraft bianca = new Aircraft()
			.name( "BIANCA" )
			.type( AircraftType.FIXEDWING )
			.make( "Hobby King" )
			.model( "Bixler 2" )
			.status( AircraftStatus.DESTROYED )
			.owner( user.id() )
			.ownerType( OwnerType.USER );
		Aircraft gemma = new Aircraft().name( "GEMMA" ).type( AircraftType.MULTIROTOR ).status( AircraftStatus.AIRWORTHY ).owner( user.id() ).ownerType( OwnerType.USER );
		Aircraft helena = new Aircraft().name( "HELENA" ).type( AircraftType.HELICOPTER ).status( AircraftStatus.INOPERATIVE ).owner( user.id() ).ownerType( OwnerType.USER );
		statePersisting.upsert( aftyn );
		statePersisting.upsert( bianca );
		statePersisting.upsert( gemma );
		statePersisting.upsert( helena );

		Battery b4s2650turnigy = new Battery()
			.name( "B 4S 2650 Turnigy" )
			.status(BatteryStatus.AVAILABLE)
			.make( "Hobby King" )
			.model( "Turnigy" )
			.connector( BatteryConnector.XT60 )
			.type( BatteryType.LIPO )
			.cells( 4 )
			.cycles( 57 )
			.capacity( 2650 )
			.owner( user.id() )
			.ownerType( OwnerType.USER );
		Battery c4s2650turnigy = new Battery()
			.name( "C 4S 2650 Turnigy" )
			.status(BatteryStatus.AVAILABLE)
			.make( "Hobby King" )
			.model( "Turnigy" )
			.connector( BatteryConnector.XT60 )
			.type( BatteryType.LIPO )
			.cells( 4 )
			.cycles( 43 )
			.capacity( 2650 )
			.owner( user.id() )
			.ownerType( OwnerType.USER );
		Battery d4s2650turnigy = new Battery()
			.name( "D 4S 2650 Turnigy" )
			.status(BatteryStatus.AVAILABLE)
			.make( "Hobby King" )
			.model( "Turnigy" )
			.connector( BatteryConnector.XT60 )
			.type( BatteryType.LIPO )
			.cells( 4 )
			.cycles( 87 )
			.capacity( 2650 )
			.owner( user.id() )
			.ownerType( OwnerType.USER );
		statePersisting.upsert( b4s2650turnigy );
		statePersisting.upsert( c4s2650turnigy );
		statePersisting.upsert( d4s2650turnigy );

		stateRetrieving.findUserTokenByPrincipal( usernameToken.principal() ).ifPresent( t -> log.warn( "Tester created=" + t.principal() ) );
	}

}
