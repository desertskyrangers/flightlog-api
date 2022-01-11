package com.desertskyrangers.flightdeck.adapter.state;

import com.desertskyrangers.flightdeck.adapter.state.entity.AircraftEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.TokenEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.VerificationEntity;
import com.desertskyrangers.flightdeck.core.model.Aircraft;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.core.model.UserToken;
import com.desertskyrangers.flightdeck.core.model.Verification;
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

	private final UserAccountRepo userAccountRepo;

	private final UserTokenRepo userTokenRepo;

	private final VerificationRepo verificationRepo;

	public StateRetrievingService( AircraftRepo aircraftRepo, UserAccountRepo userAccountRepo, UserTokenRepo userTokenRepo, VerificationRepo verificationRepo ) {
		this.aircraftRepo = aircraftRepo;
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
	public Optional<UserToken> findUserCredential( UUID id ) {
		return userTokenRepo.findById( id ).map( TokenEntity::toUserCredential );
	}

	@Override
	public Optional<UserToken> findUserTokenByPrincipal( String username ) {
		return userTokenRepo.findByPrincipal( username ).map( TokenEntity::toUserCredentialDeep );
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
