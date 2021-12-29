package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.adapter.state.entity.UserAccountEntity;
import com.desertskyrangers.flightlog.adapter.state.entity.UserCredentialEntity;
import com.desertskyrangers.flightlog.adapter.state.entity.VerificationEntity;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StateRetrievingService implements StateRetrieving {

	private final UserAccountRepo userAccountRepo;

	private final UserCredentialRepo userCredentialRepo;

	private final VerificationRepo verificationRepo;

	public StateRetrievingService( UserAccountRepo userAccountRepo, UserCredentialRepo userCredentialRepo, VerificationRepo verificationRepo ) {
		this.userAccountRepo = userAccountRepo;
		this.userCredentialRepo = userCredentialRepo;
		this.verificationRepo = verificationRepo;
	}

	@Override
	public Optional<UserCredential> findUserCredential( UUID id ) {
		return userCredentialRepo.findById( id ).map( UserCredentialEntity::toUserCredential );
	}

	@Override
	public Optional<UserCredential> findUserCredentialByUsername( String username ) {
		return userCredentialRepo.findByUsername( username ).map( UserCredentialEntity::toUserCredential );
	}

	@Override
	public Optional<UserAccount> findUserAccount( UUID id ) {
		return userAccountRepo.findById( id ).map( UserAccountEntity::toUserAccount );
	}

	@Override
	public Collection<Verification> findAllVerifications() {
		return StreamSupport.stream( verificationRepo.findAll().spliterator(), false ).map( VerificationEntity::toVerification ).collect( Collectors.toSet() );
	}

	@Override
	public Optional<Verification> findVerification( UUID id ) {
		return verificationRepo.findById( id ).map( VerificationEntity::toVerification );
	}
}
