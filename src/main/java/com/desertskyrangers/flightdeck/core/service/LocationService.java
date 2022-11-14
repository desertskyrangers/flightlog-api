package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.adapter.state.entity.LocationEntity;
import com.desertskyrangers.flightdeck.adapter.state.entity.UserEntity;
import com.desertskyrangers.flightdeck.adapter.state.repo.LocationRepo;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.core.model.LocationStatus;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.LocationServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
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

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public LocationService( StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	public Optional<Location> find( UUID id ) {
		return stateRetrieving.findLocation( id );
	}

	public Location upsert( Location location ) {
		return statePersisting.upsert( location );
	}

	public Location remove( Location location ) {
		return statePersisting.remove( location );
	}

	public Set<Location> findByUser( User user ) {
		return stateRetrieving.findLocationsByUser( user );
	}

	public Set<Location> findByUserAndStatus( User user, Set<LocationStatus> status ) {
		return stateRetrieving.findLocationsByUserAndStatus( user, status );
	}

	public Page<Location> findPageByUserAndStatus( User user, Set<LocationStatus> status, int page, int size ) {
		return stateRetrieving.findLocationsPageByUser( user, page, size );
	}

}
