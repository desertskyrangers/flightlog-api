package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.FlightUpsertRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlightService {

	Optional<Flight> find( UUID id );

	List<Flight> findByPilot( UUID owner );

	List<Flight> findFlightsByUser( UUID user );

	void upsert( FlightUpsertRequest flight );

	void remove( Flight flight );

}
