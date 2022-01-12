package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StateRetrieving {

	Optional<Aircraft> findAircraft( UUID id );

	List<Aircraft> findAircraftByOwner( UUID id );

	Optional<Battery> findBattery( UUID id );

	List<Battery> findBatteriesByOwner( UUID id );

	Optional<Flight> findFlight( UUID id );

	List<Flight> findFlightsByPilot( UUID id );

	List<Flight> findFlightsByAircraft( UUID id );

	List<Flight> findFlightsByBattery( UUID id );

	Optional<UserToken> findUserToken( UUID id );

	Optional<UserToken> findUserTokenByPrincipal( String username );

	List<User> findAllUserAccounts();

	Optional<User> findUserAccount( UUID id );

	List<Verification> findAllVerifications();

	Optional<Verification> findVerification( UUID id );
}
