package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.adapter.state.entity.MemberEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightdeck.adapter.state.repo.MemberRepo;
import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	private final MemberRepo memberRepo;

	public MemberServiceImpl( MemberRepo memberRepo ) {
		this.memberRepo = memberRepo;
	}

	@Override
	public Optional<Member> find( UUID id ) {
		return memberRepo.findById( id ).map( MemberEntity::toMember );
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
	public Set<Member> findMembershipsByUser( User user ) {
		return memberRepo.findAllByUser( UserEntity.from( user ) ).stream().map( MemberEntity::toMember ).collect( Collectors.toSet() );
	}

}
