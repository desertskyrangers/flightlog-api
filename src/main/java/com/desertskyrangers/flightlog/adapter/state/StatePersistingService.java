package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightlog.adapter.state.entity.VerificationEntity;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.StatePersisting;
import org.springframework.stereotype.Service;

@Service
public class StatePersistingService implements StatePersisting {

	private final UserAccountRepo userAccountRepo;

	private final VerificationRepo verificationRepo;

	public StatePersistingService( UserAccountRepo userAccountRepo, VerificationRepo verificationRepo ) {
		this.userAccountRepo = userAccountRepo;
		this.verificationRepo = verificationRepo;
	}

	@Override
	public void upsert( UserAccount account ) {
		userAccountRepo.save( UserEntity.from( account ) );
	}

	@Override
	public void remove( UserAccount account ) {
		userAccountRepo.deleteById( account.id() );
	}

	@Override
	public void upsert( Verification verification ) {
		verificationRepo.save( VerificationEntity.from( verification ) );
	}

	@Override
	public void remove( Verification verification ) {
		verificationRepo.deleteById( verification.id() );
	}

}
