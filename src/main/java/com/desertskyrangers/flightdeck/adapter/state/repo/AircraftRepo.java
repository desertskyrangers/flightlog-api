package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.AircraftEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface AircraftRepo extends JpaRepository<AircraftEntity, UUID> {

	List<AircraftEntity> findAircraftByOwner( UUID owner );

	Page<AircraftEntity> findAircraftByOwner( UUID owner, Pageable pageable );

	List<AircraftEntity> findAircraftByOwnerOrderByName( UUID owner );

	List<AircraftEntity> findAircraftByOwnerAndStatusOrderByName( UUID owner, String status );

}
