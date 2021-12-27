package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserProfile;
import com.desertskyrangers.flightlog.port.StatePersisting;
import org.springframework.stereotype.Service;

@Service
public class StatePersistingService implements StatePersisting {

	private final UserAccountRepo userAccountRepo;

	private final UserProfileRepo userProfileRepo;

	public StatePersistingService( UserAccountRepo userAccountRepo, UserProfileRepo userProfileRepo ) {
		this.userAccountRepo = userAccountRepo;
		this.userProfileRepo = userProfileRepo;
	}

	@Override
	public void upsert( UserAccount account ) {
		//userAccountRepo.save( account );
	}

	@Override
	public void upsert( UserProfile profile ) {
		//userProfileRepo.save( profile );
	}
}
