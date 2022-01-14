package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private UserService userService;

	@Autowired
	private User unlistedUser;

	private User mockUser;

	@BeforeEach
	void setup() {
		// Clean old flights
		statePersisting.removeAllFlights();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if( authentication != null ) {
			String username = authentication.getName();
			String password = Objects.toString( authentication.getCredentials() );

			// Delete the exising mock user account
			userService.findByPrincipal( authentication.getName() ).ifPresent( u -> userService.remove( u ) );

			// Create mock user account
			mockUser = new User();
			mockUser.lastName( username );
			mockUser.tokens( Set.of( new UserToken().principal( username ).credential( password ) ) );
			UserToken token = new UserToken().principal( username );
			mockUser.tokens( Set.of( token ) );
			token.user( mockUser );
			userService.upsert( mockUser );
		}
	}

	protected User getUnlistedUser() {
		return unlistedUser;
	}

	protected User getMockUser() {
		return mockUser;
	}

	protected Aircraft createTestAircraft() {
		Aircraft aircraft = new Aircraft();
		aircraft.id( UUID.randomUUID() );
		aircraft.name( "Aftyn" );
		aircraft.make( "Hobby King" );
		aircraft.model( "Bixler 2" );
		aircraft.type( AircraftType.FIXEDWING );
		aircraft.status( AircraftStatus.DESTROYED );
		aircraft.owner( getMockUser().id() );
		aircraft.ownerType( OwnerType.USER );
		return aircraft;
	}

	protected Battery createTestBattery() {
		Battery battery = new Battery();
		battery.name( "C 4S 2650 Turnigy" );
		battery.make( "Hobby King" );
		battery.model( "Turnigy 2650 4S" );
		battery.connector( BatteryConnector.XT60 );
		battery.status( BatteryStatus.DESTROYED );

		battery.type( BatteryType.LIPO );
		battery.cells( 4 );
		battery.cycles( 57 );
		battery.capacity( 2650 );
		battery.chargeRating( 1 );
		battery.dischargeRating( 20 );

		battery.owner( getMockUser().id() );
		battery.ownerType( OwnerType.USER );
		return battery;
	}

}
