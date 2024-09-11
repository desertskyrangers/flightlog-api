package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.LocationServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
		if( id == null ) return Optional.empty();
		Optional<Location> location = Optional.ofNullable( Location.forId( id ) );
		if( location.isEmpty() ) location = stateRetrieving.findLocation( id );
		return location;
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

	public Set<Location> findByUserAndStatus( User user, Set<Location.Status> status ) {
		return stateRetrieving.findLocationsByUserAndStatus( user, status );
	}

	public Page<Location> findPageByUserAndStatus( User user, Set<Location.Status> status, int page, int size ) {
		return stateRetrieving.findLocationsPageByUser( user, page, size );
	}

}
