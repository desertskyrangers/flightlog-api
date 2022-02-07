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

	private final GroupRepo groupRepo;

	private final MemberRepo memberRepo;

	private final TokenRepo tokenRepo;

	private final UserRepo userRepo;

	private final VerificationRepo verificationRepo;

	public StatePersistingService(
		AircraftRepo aircraftRepo, BatteryRepo batteryRepo, FlightRepo flightRepo, GroupRepo groupRepo, MemberRepo memberRepo, TokenRepo tokenRepo, UserRepo userRepo, VerificationRepo verificationRepo
	) {
		this.aircraftRepo = aircraftRepo;
		this.batteryRepo = batteryRepo;
		this.flightRepo = flightRepo;
		this.groupRepo = groupRepo;
		this.memberRepo = memberRepo;
		this.tokenRepo = tokenRepo;
		this.userRepo = userRepo;
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
	public Flight upsert( Flight flight ) {
		return FlightEntity.toFlight( flightRepo.save( FlightEntity.from( flight ) ) );
	}

	@Override
	public void remove( Flight flight ) {
		flightRepo.deleteById( flight.id() );
	}

	@Override
	public void removeAllFlights() {
		flightRepo.deleteAll();
	}

	@Override
	public Group upsert( Group group ) {
		return GroupEntity.toGroup( groupRepo.save( GroupEntity.from( group ) ) );
	}

	@Override
	public void remove( Group group ) {
		groupRepo.deleteById( group.id() );
	}

	@Override
	public void removeAllGroups() {
		groupRepo.deleteAll();
	}

	@Override
	public Member upsert( Member member ) {
		return MemberEntity.toMember( memberRepo.save( MemberEntity.from( member ) ) );
	}

	@Override
	public Member remove( Member member ) {
		memberRepo.deleteById( member.id() );
		return member;
	}

	@Override
	public void removeAllMembers() {
		memberRepo.deleteAll();
	}

	@Override
	public void upsert( UserToken token ) {
		tokenRepo.save( TokenEntity.from( token ) );
	}

	@Override
	public User upsert( User account ) {
		return UserEntity.toUser( userRepo.save( UserEntity.from( account ) ) );
	}

	@Override
	public void remove( User account ) {
		userRepo.deleteById( account.id() );
	}

	@Override
	public Verification upsert( Verification verification ) {
		verificationRepo.save( VerificationEntity.from( verification ) );
		return verification;
	}

	@Override
	public Verification remove( Verification verification ) {
		verificationRepo.deleteById( verification.id() );
		return verification;
	}

}
