package com.desertskyrangers.flightdeck.adapter.state;

import com.desertskyrangers.flightdeck.adapter.state.entity.*;
import com.desertskyrangers.flightdeck.adapter.state.repo.*;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StateRetrievingService implements StateRetrieving {

	private final AircraftRepo aircraftRepo;

	private final BatteryRepo batteryRepo;

	private final FlightRepo flightRepo;

	private final UserAccountRepo userAccountRepo;

	private final UserTokenRepo userTokenRepo;

	private final VerificationRepo verificationRepo;

	public StateRetrievingService(
		AircraftRepo aircraftRepo, BatteryRepo batteryRepo, FlightRepo flightRepo, UserAccountRepo userAccountRepo, UserTokenRepo userTokenRepo, VerificationRepo verificationRepo
	) {
		this.aircraftRepo = aircraftRepo;
		this.batteryRepo = batteryRepo;
		this.flightRepo = flightRepo;
		this.userAccountRepo = userAccountRepo;
		this.userTokenRepo = userTokenRepo;
		this.verificationRepo = verificationRepo;
	}

	@Override
	public Optional<Aircraft> findAircraft( UUID id ) {
		return aircraftRepo.findById( id ).map( AircraftEntity::toAircraft );
	}

	@Override
	public List<Aircraft> findAircraftByOwner( UUID owner ) {
		return aircraftRepo.findAircraftByOwnerOrderByName( owner ).stream().map( AircraftEntity::toAircraft ).collect( Collectors.toList() );
	}

	@Override
	public Optional<Battery> findBattery( UUID id ) {
		return batteryRepo.findById( id ).map( BatteryEntity::toBattery );
	}

	@Override
	public List<Battery> findBatteriesByOwner( UUID owner ) {
		return batteryRepo.findBatteryEntitiesByOwnerOrderByName( owner ).stream().map( BatteryEntity::toBattery ).collect( Collectors.toList() );
	}

	@Override
	public Optional<Flight> findFlight( UUID id ) {
		return flightRepo.findById( id ).map( FlightEntity::toFlight );
	}

	@Override
	public List<Flight> findFlightsByPilot( UUID id ) {
		return flightRepo.findFlightEntitiesByPilot_IdOrderByTimestampDesc( id ).stream().map( FlightEntity::toFlight ).toList();
	}

	//	@Override
	//	public List<Flight> findFlightsByObserver( UUID id ) {
	//		return null;
	//	}

	@Override
	public List<Flight> findFlightsByAircraft( UUID id ) {
		return flightRepo.findFlightEntitiesByAircraft_IdOrderByTimestampDesc( id ).stream().map( FlightEntity::toFlight ).toList();
	}

	@Override
	public List<Flight> findFlightsByBattery( UUID id ) {
		return null;
	}

	@Override
	public Optional<UserToken> findUserToken( UUID id ) {
		return userTokenRepo.findById( id ).map( TokenEntity::toUserToken );
	}

	@Override
	public Optional<UserToken> findUserTokenByPrincipal( String username ) {
		return userTokenRepo.findByPrincipal( username ).map( TokenEntity::toUserTokenDeep );
	}

	@Override
	public List<User> findAllUserAccounts() {
		return userAccountRepo.findAll().stream().map( UserEntity::toUserAccount ).collect( Collectors.toList() );
	}

	@Override
	public Optional<User> findUserAccount( UUID id ) {
		return userAccountRepo.findById( id ).map( UserEntity::toUserAccount );
	}

	@Override
	public List<Verification> findAllVerifications() {
		return StreamSupport.stream( verificationRepo.findAll().spliterator(), false ).map( VerificationEntity::toVerification ).collect( Collectors.toList() );
	}

	@Override
	public Optional<Verification> findVerification( UUID id ) {
		return verificationRepo.findById( id ).map( VerificationEntity::toVerification );
	}
}
