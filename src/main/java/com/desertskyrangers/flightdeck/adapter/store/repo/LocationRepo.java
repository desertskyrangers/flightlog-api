package com.desertskyrangers.flightdeck.adapter.store.repo;

import com.desertskyrangers.flightdeck.adapter.store.entity.LocationEntity;
import com.desertskyrangers.flightdeck.adapter.store.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface LocationRepo extends JpaRepository<LocationEntity, UUID> {

	Set<LocationEntity> findAllByUser( UserEntity user );

	Page<LocationEntity> findAllPageByUser( UserEntity user, Pageable pageable );

	Set<LocationEntity> findAllByUserAndStatusIn( UserEntity user, Set<String> status );

	Page<LocationEntity> findAllPageByUserAndStatusIn( UserEntity user, Set<String> status, Pageable pageable );

}
