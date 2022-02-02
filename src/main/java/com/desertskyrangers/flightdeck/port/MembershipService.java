package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.MemberStatus;
import com.desertskyrangers.flightdeck.core.model.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface MembershipService {

	Optional<Member> find( UUID id );

	Member upsert( Member member );

	Member remove( Member member );

	Set<Member> findMembershipsByUser( User user );

	void requestMembership( User user, Group group, MemberStatus status );

	void cancelMembership( Member member );

}
