package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.adapter.state.entity.TokenEntity;
import com.desertskyrangers.flightlog.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightlog.adapter.state.entity.VerificationEntity;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserToken;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StateRetrievingService implements StateRetrieving {

	private final UserAccountRepo userAccountRepo;

	private final UserTokenRepo userTokenRepo;

	private final VerificationRepo verificationRepo;

	public StateRetrievingService( UserAccountRepo userAccountRepo, UserTokenRepo userTokenRepo, VerificationRepo verificationRepo ) {
		this.userAccountRepo = userAccountRepo;
		this.userTokenRepo = userTokenRepo;
		this.verificationRepo = verificationRepo;
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
	public List<UserAccount> findAllUserAccounts() {
		return userAccountRepo.findAll().stream().map( UserEntity::toUserAccount ).collect( Collectors.toList() );
	}

	@Override
	public Optional<UserAccount> findUserAccount( UUID id ) {
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
