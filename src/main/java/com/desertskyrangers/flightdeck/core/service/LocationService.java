package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.adapter.state.entity.LocationEntity;
import com.desertskyrangers.flightdeck.adapter.state.repo.LocationRepo;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.port.LocationServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

}
