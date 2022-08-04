package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.GroupEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.MemberEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface MemberRepo extends JpaRepository<MemberEntity, UUID> {

	Optional<MemberEntity> findByGroupAndUser( GroupEntity grouo, UserEntity user );

	Set<MemberEntity> findAllByUser_IdAndStatus( UUID id, String status );

	Page<MemberEntity> findAllByUser_IdAndStatus( UUID id, String status, Pageable pageable );

	Set<MemberEntity> findAllByGroup_IdAndStatus( UUID id, String status );

	Set<MemberEntity> findAllByUser( UserEntity user );

	Set<MemberEntity> findAllByGroup( GroupEntity group );

}
