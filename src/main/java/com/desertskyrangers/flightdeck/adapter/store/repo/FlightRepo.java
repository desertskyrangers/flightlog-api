package com.desertskyrangers.flightdeck.adapter.store.repo;

import com.desertskyrangers.flightdeck.adapter.store.entity.AircraftEntity;
import com.desertskyrangers.flightdeck.adapter.store.entity.BatteryEntity;
import com.desertskyrangers.flightdeck.adapter.store.entity.FlightEntity;
import com.desertskyrangers.flightdeck.adapter.store.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlightRepo extends JpaRepository<FlightEntity, UUID> {

	List<FlightEntity> findFlightEntitiesByPilot_IdOrderByTimestampDesc( UUID id );

	Page<FlightEntity> findFlightEntitiesByPilot_IdOrderByTimestampDesc( UUID id, Pageable pageable );

	List<FlightEntity> findFlightEntitiesByObserver_IdOrderByTimestampDesc( UUID id );

	Page<FlightEntity> findFlightEntitiesByObserver_IdOrderByTimestampDesc( UUID id, Pageable pageable );

	List<FlightEntity> findFlightEntitiesByAircraft_IdOrderByTimestampDesc( UUID id );

	Page<FlightEntity> findFlightEntitiesByAircraft_IdOrderByTimestampDesc( UUID id, Pageable pageable );

	// Pilot
	List<FlightEntity> findFlightEntitiesByPilotIdAndTimestampAfterOrderByTimestampDesc( UUID id, long timestamp );

	List<FlightEntity> findAllByPilotId( UUID id, Pageable pageable );

	// Observer
	List<FlightEntity> findFlightEntitiesByObserverIdAndTimestampAfterOrderByTimestampDesc( UUID id, long timestamp );

	List<FlightEntity> findAllByObserverId( UUID id, Pageable pageable );

	// Owner
	List<FlightEntity> findFlightEntitiesByAircraft_OwnerAndTimestampAfterOrderByTimestampDesc( UUID id, long timestamp );

	List<FlightEntity> findAllByAircraft_Owner( UUID id, Pageable pageable );

	// All flights by role
	Page<FlightEntity> findFlightEntitiesByPilot_IdOrObserver_IdOrAircraft_OwnerOrderByTimestampDesc( UUID pilotId, UUID observerId, UUID aircraftOwnerId, Pageable pageable );

	Integer countByPilot( UserEntity user );

	@Deprecated
	Integer countByPilot_Id( UUID id );

	@Query( "select sum(f.duration) from FlightEntity f where f.pilot = ?1" )
	Long getFlightTimeByPilot( UserEntity user );

	@Query( "select sum(f.duration) from FlightEntity f where f.pilot.id = ?1" )
	@Deprecated
	Long getFlightTimeByPilot_Id( UUID id );

	@Query( "select count(f) from FlightEntity f where f.observer = ?1 and f.pilot <> ?1" )
	Integer getFlightCountByObserver( UserEntity user );

	@Query( "select count(f) from FlightEntity f where f.observer.id = ?1 and f.pilot.id <> ?1" )
	@Deprecated
	Integer getFlightCountByObserver_Id( UUID id );

	@Query( "select sum(f.duration) from FlightEntity f where f.observer = ?1 and f.pilot <> ?1" )
	Long getFlightTimeByObserver( UserEntity user );

	@Query( "select sum(f.duration) from FlightEntity f where f.observer.id = ?1 and f.pilot.id <> ?1" )
	@Deprecated
	Long getFlightTimeByObserver_Id( UUID id );

	Optional<FlightEntity> findFirstByAircraftOrderByTimestampDesc( AircraftEntity aircraft );

	Optional<FlightEntity> findFirstByPilotOrderByTimestampDesc( UserEntity pilot );

	Optional<FlightEntity> findFirstByAircraftOrderByDurationDesc( AircraftEntity aircraft );

	Optional<FlightEntity> findFirstByPilotOrderByDurationDesc( UserEntity pilot );

	Integer countByAircraft_Id( UUID id );

	@Query( "select sum(f.duration) from FlightEntity f where f.aircraft.id = ?1" )
	Long getFlightTimeByAircraft_Id( UUID id );

	@Query( "select count(f) from FlightEntity f where ?1 member of f.batteries" )
	Integer countByBattery( BatteryEntity battery );

	@Query( "select sum(f.duration) from FlightEntity f where ?1 member of f.batteries" )
	Long getFlightTimeByBattery( BatteryEntity battery);

}
