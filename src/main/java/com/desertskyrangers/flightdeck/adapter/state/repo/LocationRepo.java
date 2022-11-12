package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.LocationEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface LocationRepo extends JpaRepository<LocationEntity, UUID> {

	Page<LocationEntity> findLocationPageByUserAndStatusIn( UserEntity user, Set<String> status, Pageable pageable );

}
