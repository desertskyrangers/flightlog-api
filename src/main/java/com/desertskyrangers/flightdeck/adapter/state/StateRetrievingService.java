package com.desertskyrangers.flightdeck.adapter.state;

import com.desertskyrangers.flightdeck.adapter.state.entity.*;
import com.desertskyrangers.flightdeck.adapter.state.repo.*;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StateRetrievingService implements StateRetrieving {

	private final AircraftRepo aircraftRepo;

	private final BatteryRepo batteryRepo;

	private final FlightRepo flightRepo;

	private final GroupRepo groupRepo;

	private final UserRepo userRepo;

	private final TokenRepo tokenRepo;

	private final VerificationRepo verificationRepo;

	public StateRetrievingService(
		AircraftRepo aircraftRepo, BatteryRepo batteryRepo, FlightRepo flightRepo, GroupRepo groupRepo, UserRepo userRepo, TokenRepo tokenRepo, VerificationRepo verificationRepo
	) {
		this.aircraftRepo = aircraftRepo;
		this.batteryRepo = batteryRepo;
		this.flightRepo = flightRepo;
		this.groupRepo = groupRepo;
		this.userRepo = userRepo;
		this.tokenRepo = tokenRepo;
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

	@Override
	public List<Flight> findFlightsByObserver( UUID id ) {
		return flightRepo.findFlightEntitiesByObserver_IdOrderByTimestampDesc( id ).stream().map( FlightEntity::toFlight ).toList();
	}

	@Override
	public List<Flight> findFlightsByAircraft( UUID id ) {
		return flightRepo.findFlightEntitiesByAircraft_IdOrderByTimestampDesc( id ).stream().map( FlightEntity::toFlight ).toList();
	}

	@Override
	public List<Flight> findFlightsByBattery( UUID id ) {
		return null;
	}

	/**
	 * This returns the list of flights where the user is a pilot, observer, or aircraft owner.
	 *
	 * @param id The user id
	 * @return The list of flights
	 */
	@Override
	public List<Flight> findFlightsByUser( UUID id ) {
		return flightRepo.findFlightEntitiesByPilot_IdOrObserver_IdOrAircraft_OwnerOrderByTimestampDesc( id, id, id ).stream().map( FlightEntity::toFlight ).toList();
	}

	@Override
	public Optional<Group> findGroup( UUID id ) {
		return groupRepo.findById( id ).map( GroupEntity::toGroup );
	}

	@Override
	public Set<Group> findGroupsByUser( UUID id ) {
		return userRepo.findById( id ).map( UserEntity::getGroups ).stream().flatMap( Collection::stream ).map( GroupEntity::toGroup ).collect( Collectors.toSet());
	}

	@Override
	public Optional<UserToken> findUserToken( UUID id ) {
		return tokenRepo.findById( id ).map( TokenEntity::toUserToken );
	}

	@Override
	public Optional<UserToken> findUserTokenByPrincipal( String username ) {
		return tokenRepo.findByPrincipal( username ).map( TokenEntity::toUserTokenDeep );
	}

	@Override
	public List<User> findAllUserAccounts() {
		return userRepo.findAll().stream().map( UserEntity::toUser ).collect( Collectors.toList() );
	}

	@Override
	public Optional<User> findUser( UUID id ) {
		if( id == null ) return Optional.empty();
		return userRepo.findById( id ).map( UserEntity::toUser );
	}

	@Override
	public List<Verification> findAllVerifications() {
		return StreamSupport.stream( verificationRepo.findAll().spliterator(), false ).map( VerificationEntity::toVerification ).collect( Collectors.toList() );
	}

	@Override
	public Optional<Verification> findVerification( UUID id ) {
		return verificationRepo.findById( id ).map( VerificationEntity::toVerification );
	}

	@Override
	public int getPilotFlightCount( UUID id ) {
		Integer count = flightRepo.countByPilot_Id( id );
		return count == null ? 0 : count;
	}

	@Override
	public long getPilotFlightTime( UUID id ) {
		Long time = flightRepo.getFlightTimeByPilot_Id( id );
		return time == null ? 0 : flightRepo.getFlightTimeByPilot_Id( id );
	}

}
