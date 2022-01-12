package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.port.FlightService;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FlightServiceImpl implements FlightService {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public FlightServiceImpl( StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public Optional<Flight> find( UUID id ) {
		return stateRetrieving.findFlight( id );
	}

	@Override
	public List<Flight> findByPilot( UUID pilot ) {
		return stateRetrieving.findFlightsByPilot( pilot );
	}

	@Override
	public void upsert( Flight flight ) {
		statePersisting.upsert( flight );
	}

	@Override
	public void remove( Flight flight ) {
		statePersisting.remove( flight );
	}
}
