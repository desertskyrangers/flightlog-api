package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface MemberService {

	Optional<Member> find( UUID id );

	Member upsert( Member member );

	Member remove( Member member );

	Set<Member> findMembershipsByUser( User user );

}
