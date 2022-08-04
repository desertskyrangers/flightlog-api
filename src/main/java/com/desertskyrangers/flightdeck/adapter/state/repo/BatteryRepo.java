package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.BatteryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BatteryRepo extends JpaRepository<BatteryEntity, UUID> {

	List<BatteryEntity> findBatteryEntityByOwner( UUID owner );

	List<BatteryEntity> findBatteryEntitiesByOwnerOrderByName( UUID owner );

	Page<BatteryEntity> findBatteryEntitiesByOwnerOrderByName( UUID owner, Pageable pageable );

}
