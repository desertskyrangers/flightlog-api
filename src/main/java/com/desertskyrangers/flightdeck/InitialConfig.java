package com.desertskyrangers.flightdeck;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@Configuration
@Slf4j
public class InitialConfig {

	private static final UUID UNLISTED_USER_ID = UUID.fromString( "6e0c4460-357b-4a86-901d-e2ba16000c59" );

	private final FlightDeckApp app;

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private final AircraftServices aircraftServices;

	private final BatteryServices batteryServices;

	private final DashboardServices dashboardServices;

	private User unlisted;

	public InitialConfig( FlightDeckApp app, StatePersisting statePersisting, StateRetrieving stateRetrieving, AircraftServices aircraftServices, BatteryServices batteryServices, DashboardServices dashboardServices ) {
		this.app = app;
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
		this.aircraftServices = aircraftServices;
		this.batteryServices = batteryServices;
		this.dashboardServices = dashboardServices;
	}

	@Bean
	User unlistedUser() {
		if( unlisted == null ) {
			Optional<User> optional = stateRetrieving.findUser( UNLISTED_USER_ID );
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

	void run() {
		User unlisted = new User();
		unlisted.id( UUID.fromString( "6e0c4460-357b-4a86-901d-e2ba16000c59" ) );
		unlisted.lastName( "Unlisted" );
		unlisted.preferredName( "Unlisted" );
		Optional<User> optional = stateRetrieving.findUser( unlisted.id() );
		if( optional.isEmpty() ) statePersisting.upsert( unlisted );

		if( app.isProduction() ) return;

		User tia = createTiaTest();
		User tom = createTomTest();
		User tea = createTeaTest();
		User tim = createTimTest();

		Aircraft aftyn = statePersisting.upsert( createAftyn().owner( tia.id() ).ownerType( OwnerType.USER ) );
		Aircraft bianca = statePersisting.upsert( createBianca().owner( tia.id() ).ownerType( OwnerType.USER ) );
		Aircraft gemma = statePersisting.upsert( createGemma().owner( tia.id() ).ownerType( OwnerType.USER ) );
		Aircraft helena = statePersisting.upsert( createHelena().owner( tia.id() ).ownerType( OwnerType.USER ) );

		Battery a4s2650turnigy = new Battery()
			.name( "A 4S 2650 Turnigy" )
			.status( BatteryStatus.DESTROYED )
			.make( "Hobby King" )
			.model( "Turnigy" )
			.connector( BatteryConnector.XT60 )
			.type( BatteryType.LIPO )
			.cells( 4 )
			.initialCycles( 255 )
			.capacity( 2650 )
			.owner( tia.id() )
			.ownerType( OwnerType.USER );
		Battery b4s2650turnigy = new Battery()
			.name( "B 4S 2650 Turnigy" )
			.status( BatteryStatus.AVAILABLE )
			.make( "Hobby King" )
			.model( "Turnigy" )
			.connector( BatteryConnector.XT60 )
			.type( BatteryType.LIPO )
			.cells( 4 )
			.initialCycles( 87 )
			.capacity( 2650 )
			.owner( tia.id() )
			.ownerType( OwnerType.USER );
		Battery c4s2650turnigy = new Battery()
			.name( "C 4S 2650 Turnigy" )
			.status( BatteryStatus.AVAILABLE )
			.make( "Hobby King" )
			.model( "Turnigy" )
			.connector( BatteryConnector.XT60 )
			.type( BatteryType.LIPO )
			.cells( 4 )
			.initialCycles( 57 )
			.capacity( 2650 )
			.owner( tia.id() )
			.ownerType( OwnerType.USER );
		Battery d4s2650turnigy = new Battery()
			.name( "D 4S 2650 Turnigy" )
			.status( BatteryStatus.AVAILABLE )
			.make( "Hobby King" )
			.model( "Turnigy" )
			.connector( BatteryConnector.XT60 )
			.type( BatteryType.LIPO )
			.cells( 4 )
			.initialCycles( 43 )
			.capacity( 2650 )
			.owner( tia.id() )
			.ownerType( OwnerType.USER );
		statePersisting.upsert( a4s2650turnigy );
		statePersisting.upsert( b4s2650turnigy );
		statePersisting.upsert( c4s2650turnigy );
		statePersisting.upsert( d4s2650turnigy );

		long timestamp = System.currentTimeMillis() - 3600000;
		statePersisting.upsert( new Flight().pilot( tia ).observer( tia ).aircraft( aftyn ).batteries( Set.of( d4s2650turnigy ) ).timestamp( timestamp += 600000 ).duration( 240 ) );
		statePersisting.upsert( new Flight().pilot( tia ).observer( tia ).aircraft( bianca ).timestamp( timestamp += 600000 ).duration( 54 ) );
		statePersisting.upsert( new Flight().pilot( tom ).observer( tia ).aircraft( bianca ).timestamp( timestamp += 600000 ).duration( 163 ) );
		statePersisting.upsert( new Flight().pilot( tea ).observer( tia ).aircraft( aftyn ).timestamp( timestamp += 600000 ).duration( 182 ) );
		statePersisting.upsert( new Flight().pilot( tim ).observer( tea ).aircraft( aftyn ).timestamp( timestamp += 600000 ).duration( 34 ) );

		Group testersInfinite = new Group().name( "Testers Infinite" ).type( GroupType.GROUP );
		statePersisting.upsert( testersInfinite );

		Group testersUnlimited = new Group().name( "Testers Unlimited" ).type( GroupType.GROUP );
		statePersisting.upsert( testersUnlimited );

		Group testersAroundTheClock = new Group().name( "Testers Around the Clock" ).type( GroupType.GROUP );
		statePersisting.upsert( testersAroundTheClock );

		statePersisting.upsert( new Member().user( tia ).group( testersInfinite ).status( MemberStatus.OWNER ) );
		statePersisting.upsert( new Member().user( tia ).group( testersAroundTheClock ).status( MemberStatus.ACCEPTED ) );

		statePersisting.upsert( new Member().user( tom ).group( testersUnlimited ).status( MemberStatus.OWNER ) );
		statePersisting.upsert( new Member().user( tom ).group( testersInfinite ).status( MemberStatus.ACCEPTED ) );

		log.warn( "Test data created!" );
	}

	private User createTiaTest() {
		User user = new User();
		user.id( UUID.fromString( "394de1f6-5071-4b8e-a31e-795d2f22d513" ) );
		user.username( "tia" );
		user.firstName( "Tia" );
		user.lastName( "Test" );
		user.preferredName( "Tia Test" );
		user.email( "tiat@noreply.com" );
		user.smsNumber( "800-555-8428" );
		user.smsCarrier( SmsCarrier.SPRINT );
		statePersisting.upsert( user );

		String credential = new BCryptPasswordEncoder().encode( "tester" );
		UserToken usernameToken = new UserToken().user( user ).principal( user.username() ).credential( credential );
		UserToken emailToken = new UserToken().user( user ).principal( user.email() ).credential( credential );
		user.tokens( Set.of( usernameToken, emailToken ) );
		statePersisting.upsert( user );

		Map<String, Object> preferences = new HashMap<>();
		preferences.put( PreferenceKey.SHOW_AIRCRAFT_STATS, true );
		statePersisting.upsertPreferences( user, preferences );

		return user;
	}

	private User createTomTest() {
		User user = new User();
		user.id( UUID.fromString( "3943734d-7ab9-4cad-9d6d-9ca50335ad1c" ) );
		user.username( "tom" );
		user.firstName( "Tom" );
		user.lastName( "Test" );
		user.preferredName( "Tommy Test" );
		user.email( "tomt@noreply.com" );
		user.smsNumber( "800-555-8668" );
		user.smsCarrier( SmsCarrier.SPRINT );
		statePersisting.upsert( user );

		String credential = new BCryptPasswordEncoder().encode( "tester" );
		UserToken usernameToken = new UserToken().user( user ).principal( user.username() ).credential( credential );
		UserToken emailToken = new UserToken().user( user ).principal( user.email() ).credential( credential );
		user.tokens( Set.of( usernameToken, emailToken ) );
		statePersisting.upsert( user );

		return user;
	}

	private User createTeaTest() {
		User user = new User();
		user.id( UUID.fromString( "c70be671-b8ff-4f6a-926f-37149840385e" ) );
		user.username( "tea" );
		user.firstName( "Téa" );
		user.lastName( "Test" );
		user.preferredName( "Téa Test" );
		user.email( "teat@noreply.com" );
		user.smsNumber( "800-555-8328" );
		user.smsCarrier( SmsCarrier.SPRINT );
		statePersisting.upsert( user );

		String credential = new BCryptPasswordEncoder().encode( "tester" );
		UserToken usernameToken = new UserToken().user( user ).principal( user.username() ).credential( credential );
		UserToken emailToken = new UserToken().user( user ).principal( user.email() ).credential( credential );
		user.tokens( Set.of( usernameToken, emailToken ) );
		statePersisting.upsert( user );

		return user;
	}

	private User createTimTest() {
		User user = new User();
		user.id( UUID.fromString( "159a5224-9517-4776-886a-bc9d5e032a2a" ) );
		user.username( "tim" );
		user.firstName( "Tim" );
		user.lastName( "Test" );
		user.preferredName( "Timmy Test" );
		user.email( "timt@noreply.com" );
		user.smsNumber( "800-555-8468" );
		user.smsCarrier( SmsCarrier.SPRINT );
		statePersisting.upsert( user );

		String credential = new BCryptPasswordEncoder().encode( "tester" );
		UserToken usernameToken = new UserToken().user( user ).principal( user.username() ).credential( credential );
		UserToken emailToken = new UserToken().user( user ).principal( user.email() ).credential( credential );
		user.tokens( Set.of( usernameToken, emailToken ) );
		statePersisting.upsert( user );

		return user;
	}

	private Aircraft createAftyn() {
		return new Aircraft()
			.name( "AFTYN" )
			.type( AircraftType.FIXEDWING )
			.make( "Hobby King" )
			.model( "Bixler 2" )
			.status( AircraftStatus.AIRWORTHY )
			.wingspan( 1500 )
			.length( 963 )
			.wingarea( 2500 )
			.weight( 960 )
			.baseColor(AppColor.WHITE)
			.trimColor(AppColor.BLUE);
	}

	private Aircraft createBianca() {
		return new Aircraft()
			.name( "BIANCA" )
			.type( AircraftType.FIXEDWING )
			.make( "Hobby King" )
			.model( "Bixler 2" )
			.status( AircraftStatus.DESTROYED )
			.wingspan( 1500 )
			.length( 963 )
			.wingarea( 2500 )
			.weight( 960 );
	}

	private Aircraft createGemma() {
		return new Aircraft().name( "GEMMA" ).type( AircraftType.MULTIROTOR ).status( AircraftStatus.AIRWORTHY );
	}

	private Aircraft createHelena() {
		return new Aircraft().name( "HOPE" ).type( AircraftType.HELICOPTER ).status( AircraftStatus.INOPERATIVE );
	}

}
