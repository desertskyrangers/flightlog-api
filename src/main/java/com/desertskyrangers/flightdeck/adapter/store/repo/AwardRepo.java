package com.desertskyrangers.flightdeck.adapter.store.repo;

import com.desertskyrangers.flightdeck.adapter.store.entity.AwardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AwardRepo extends JpaRepository<AwardEntity, UUID> {

	Page<AwardEntity> findAwardEntitiesByRecipientId( UUID id, Pageable pageable );

}
