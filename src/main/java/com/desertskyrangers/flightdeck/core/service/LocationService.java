package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.adapter.state.entity.LocationEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightdeck.adapter.state.repo.LocationRepo;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.core.model.LocationStatus;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.LocationServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocationService implements LocationServices {

	private final LocationRepo locationRepo;

	public LocationService( LocationRepo locationRepo ) {
		this.locationRepo = locationRepo;
	}

	public Optional<Location> find( UUID id ) {
		return locationRepo.findById( id ).map( LocationEntity::toLocation );
	}

	public Location upsert( Location location ) {
		return LocationEntity.toLocation( locationRepo.save( LocationEntity.from( location ) ) );
	}

	public Location remove( Location location ) {
		locationRepo.delete( LocationEntity.from( location ) );
		return location;
	}

	public Set<Location> findByUser( User user ) {
		return locationRepo.findAllByUser( UserEntity.from( user ) ).stream().map( LocationEntity::toLocation ).collect( Collectors.toSet() );
	}

	public Page<Location> findPageByUserAndStatus( User user, Set<LocationStatus> status, int page, int size ) {
		Set<String> statusValues = status.stream().map( s -> s.name().toLowerCase() ).collect( Collectors.toSet() );
		return locationRepo.findLocationPageByUserAndStatusIn( UserEntity.from( user ), statusValues, PageRequest.of( page, size ) ).map( LocationEntity::toLocation );
	}

}
