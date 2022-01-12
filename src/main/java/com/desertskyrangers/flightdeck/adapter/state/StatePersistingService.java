package com.desertskyrangers.flightdeck.adapter.state;

import com.desertskyrangers.flightdeck.adapter.state.entity.*;
import com.desertskyrangers.flightdeck.adapter.state.repo.*;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import org.springframework.stereotype.Service;

@Service
public class StatePersistingService implements StatePersisting {

	private final AircraftRepo aircraftRepo;

	private final BatteryRepo batteryRepo;

	private final FlightRepo flightRepo;

	private final UserAccountRepo userAccountRepo;

	private final VerificationRepo verificationRepo;

	public StatePersistingService( AircraftRepo aircraftRepo, BatteryRepo batteryRepo, FlightRepo flightRepo, UserAccountRepo userAccountRepo, VerificationRepo verificationRepo ) {
		this.aircraftRepo = aircraftRepo;
		this.batteryRepo = batteryRepo;
		this.flightRepo = flightRepo;
		this.userAccountRepo = userAccountRepo;
		this.verificationRepo = verificationRepo;
	}

	@Override
	public void upsert( Aircraft aircraft ) {
		aircraftRepo.save( AircraftEntity.from( aircraft ) );
	}

	@Override
	public void remove( Aircraft aircraft ) {
		aircraftRepo.deleteById( aircraft.id() );
	}

	@Override
	public void upsert( Battery battery ) {
		batteryRepo.save( BatteryEntity.from( battery ) );
	}

	@Override
	public void remove( Battery battery ) {
		batteryRepo.deleteById( battery.id() );
	}

	@Override
	public void upsert( Flight flight ) {
		flightRepo.save( FlightEntity.from( flight ) );
	}

	@Override
	public void remove( Flight flight ) {
		flightRepo.deleteById( flight.id() );
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

}
