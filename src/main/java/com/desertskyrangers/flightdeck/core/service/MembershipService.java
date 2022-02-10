package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.exception.UnauthorizedException;
import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.MemberStatus;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.MembershipServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class MembershipService implements MembershipServices {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public MembershipService( StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public Optional<Member> find( UUID id ) {
		return stateRetrieving.findMembership( id );
	}

	@Override
	public Member upsert( User requester, Member member ) {
		Member current = stateRetrieving.findMembership( requester.id() ).orElse( null );

		boolean isOwned = stateRetrieving.findGroupOwners( member.group() ).contains( requester );
		boolean isAccepting = current != null && current.status() == MemberStatus.INVITED && member.status() == MemberStatus.ACCEPTED;

		if( isOwned || isAccepting ) return statePersisting.upsert( member );

		throw new UnauthorizedException( requester );
	}

	@Override
	public Member remove( User requester, Member member ) {
		return statePersisting.remove( member );
	}

	@Override
	public Set<Member> findMembershipsByUser( User user ) {
		return stateRetrieving.findMemberships( user );
	}

	public Set<Member> findMembershipsByGroup( Group group ) {
		return stateRetrieving.findMemberships( group );
	}

	public Member requestMembership( User requester, User user, Group group, MemberStatus status ) {
		return upsert( requester, new Member().user( user ).group( group ).status( status ) );
	}

	public Member cancelMembership( User requester, Member member ) {
		remove( requester, member );
		return member;
	}

}
