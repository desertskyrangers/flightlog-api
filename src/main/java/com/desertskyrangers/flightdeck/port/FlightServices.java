package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Aircraft;
import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.FlightUpsertRequest;
import com.desertskyrangers.flightdeck.core.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlightServices {

	Optional<Flight> find( UUID id );

	List<Flight> findByPilot( User pilot );

	List<Flight> findFlightsByUser( User user );

	void upsert( FlightUpsertRequest flight );

	void remove( Flight flight );

	int getPilotFlightCount( UUID user );

	long getPilotFlightTime( UUID user );

	int getObserverFlightCount( UUID user );

	long getObserverFlightTime( UUID user );

	int getAircraftFlightCount( Aircraft aircraft );

	long getAircraftFlightTime( Aircraft aircraft );

}
