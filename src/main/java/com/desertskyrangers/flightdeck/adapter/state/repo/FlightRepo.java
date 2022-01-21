package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FlightRepo extends JpaRepository<FlightEntity, UUID> {

	List<FlightEntity> findFlightEntitiesByPilot_IdOrderByTimestampDesc( UUID id );

	List<FlightEntity> findFlightEntitiesByObserver_IdOrderByTimestampDesc( UUID id );

	List<FlightEntity> findFlightEntitiesByAircraft_IdOrderByTimestampDesc( UUID id );

	List<FlightEntity> findFlightEntitiesByAircraft_OwnerOrderByTimestampDesc( UUID id );

	List<FlightEntity> findFlightEntitiesByPilot_IdOrObserver_IdOrAircraft_OwnerOrderByTimestampDesc( UUID pilotId, UUID observerId, UUID aircraftOwnerId );

	Integer countByPilot_Id( UUID id );

	@Query("select sum(f.duration) from FlightEntity f where f.pilot.id = ?1")
	Long getFlightTimeByPilot_Id(UUID id);

}
