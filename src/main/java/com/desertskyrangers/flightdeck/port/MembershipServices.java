package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.MemberStatus;
import com.desertskyrangers.flightdeck.core.model.User;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface MembershipServices {

	Optional<Member> find( UUID id );

	Member upsert( User requester, Member member );

	Member remove( User requester, Member member );

	Set<Member> findMembershipsByUser( User user );

	Set<Member> findMembershipsByGroup( Group group );

	Member requestMembership( User requester, User user, Group group, MemberStatus status );

	Member cancelMembership( User requester, Member member );

}
