package com.desertskyrangers.flightdeck.adapter.store.repo;

import com.desertskyrangers.flightdeck.adapter.store.entity.BatteryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface BatteryRepo extends JpaRepository<BatteryEntity, UUID> {

	List<BatteryEntity> findBatteryEntityByOwner( UUID owner );

	List<BatteryEntity> findBatteryEntitiesByOwnerOrderByName( UUID owner );

	Page<BatteryEntity> findBatteryEntitiesByOwnerOrderByName( UUID owner, Pageable pageable );

	Page<BatteryEntity> findBatteryEntitiesByOwnerAndStatusInOrderByName( UUID owner, Set<String> status, Pageable pageable );

}
