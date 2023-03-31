package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.AwardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AwardRepo extends JpaRepository<AwardEntity, UUID> {

	Page<AwardEntity> findAwardEntitiesByRecipient( UUID owner, Pageable pageable );

}
