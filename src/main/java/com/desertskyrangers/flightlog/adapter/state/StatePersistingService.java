package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.adapter.state.entity.AircraftEntity;
import com.desertskyrangers.flightlog.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightlog.adapter.state.entity.VerificationEntity;
import com.desertskyrangers.flightlog.core.model.Aircraft;
import com.desertskyrangers.flightlog.core.model.User;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.StatePersisting;
import org.springframework.stereotype.Service;

@Service
public class StatePersistingService implements StatePersisting {

	private final UserAccountRepo userAccountRepo;

	private final VerificationRepo verificationRepo;

	private final AircraftRepo aircraftRepo;

	public StatePersistingService( UserAccountRepo userAccountRepo, VerificationRepo verificationRepo, AircraftRepo aircraftRepo ) {
		this.userAccountRepo = userAccountRepo;
		this.verificationRepo = verificationRepo;
		this.aircraftRepo = aircraftRepo;
	}

	@Override
	public void upsert( User account ) {
		userAccountRepo.save( UserEntity.from( account ) );
	}

	@Override
	public void remove( User account ) {
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

	@Override
	public void upsert( Aircraft aircraft ) {
		aircraftRepo.save( AircraftEntity.from( aircraft ) );
	}

	@Override
	public void remove( Aircraft aircraft ) {
		aircraftRepo.deleteById( aircraft.id() );
	}

}
