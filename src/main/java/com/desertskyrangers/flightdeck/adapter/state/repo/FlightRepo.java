package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlightRepo extends JpaRepository<FlightEntity, UUID> {

	List<FlightEntity> findFlightEntitiesByPilot_IdOrderByTimestampDesc(UUID id);

	List<FlightEntity> findFlightEntitiesByAircraft_IdOrderByTimestampDesc( UUID id );

}
