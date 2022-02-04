package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.MemberStatus;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.MembershipService;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class MembershipServiceImpl implements MembershipService {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public MembershipServiceImpl( StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public Optional<Member> find( UUID id ) {
		return stateRetrieving.findMembership( id );
	}

	@Override
	public Member upsert( Member member ) {
		return statePersisting.upsert( member );
	}

	@Override
	public Member remove( Member member ) {
		return statePersisting.remove( member );
	}

	@Override
	public Set<Member> findMembershipsByUser( User user ) {
		return stateRetrieving.findMemberships( user );
	}

	public Set<Member> findMembershipsByGroup( Group group ) {
		Set<Member> members = stateRetrieving.findMemberships( group );
		log.warn( "{} member count={}", group.name(), members.size() );
		return members;
	}

	public void requestMembership( User user, Group group, MemberStatus status ) {
		upsert( new Member().user( user ).group( group ).status( status ) );
	}

	public void cancelMembership( Member member ) {
		remove( member );
	}

}
