package com.desertskyrangers.flightdeck;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Configuration
@Slf4j
public class InitialConfig {

	private static final UUID UNLISTED_USER_ID = UUID.fromString( "6e0c4460-357b-4a86-901d-e2ba16000c59" );

	private final FlightDeckApp app;

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private User unlisted;

	public InitialConfig( FlightDeckApp app, StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.app = app;
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	@Bean
	User unlistedUser() {
		if( unlisted == null ) {
			Optional<User> optional = stateRetrieving.findUserAccount( UNLISTED_USER_ID );
			if( optional.isPresent() ) {
				unlisted = optional.get();
			} else {
				unlisted = new User();
				unlisted.id( UNLISTED_USER_ID );
				unlisted.lastName( "Unlisted" );
				unlisted.preferredName( "Unlisted" );
				statePersisting.upsert( unlisted );
			}
		}

		return unlisted;
	}

	void init() {
		User unlisted = new User();
		unlisted.id( UUID.fromString( "6e0c4460-357b-4a86-901d-e2ba16000c59" ) );
		unlisted.lastName( "Unlisted" );
		unlisted.preferredName( "Unlisted" );
		Optional<User> optional = stateRetrieving.findUserAccount( unlisted.id() );
		if( optional.isEmpty() ) statePersisting.upsert( unlisted );

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
			.status( BatteryStatus.AVAILABLE )
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
			.status( BatteryStatus.AVAILABLE )
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
			.status( BatteryStatus.AVAILABLE )
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

		stateRetrieving.findUserTokenByPrincipal( usernameToken.principal() ).ifPresent( t -> log.warn( "Testing data created" ) );
	}

}
