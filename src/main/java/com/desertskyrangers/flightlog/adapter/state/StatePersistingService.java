package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.adapter.state.entity.UserCredentialEntity;
import com.desertskyrangers.flightlog.adapter.state.entity.UserAccountEntity;
import com.desertskyrangers.flightlog.adapter.state.entity.VerificationEntity;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.StatePersisting;
import org.springframework.stereotype.Service;

@Service
public class StatePersistingService implements StatePersisting {

	private final UserCredentialRepo userCredentialRepo;

	private final UserAccountRepo userAccountRepo;

	private final VerificationRepo verificationRepo;

	public StatePersistingService( UserCredentialRepo userCredentialRepo, UserAccountRepo userAccountRepo, VerificationRepo verificationRepo ) {
		this.userCredentialRepo = userCredentialRepo;
		this.userAccountRepo = userAccountRepo;
		this.verificationRepo = verificationRepo;
	}

	@Override
	public void upsert( UserCredential account ) {
		userCredentialRepo.save( UserCredentialEntity.from( account ) );
	}

	@Override
	public void upsert( UserAccount profile ) {
		userAccountRepo.save( UserAccountEntity.from( profile ) );
	}

	@Override
	public void upsert( Verification verification ) {
		verificationRepo.save( VerificationEntity.from( verification ) );
	}
}
