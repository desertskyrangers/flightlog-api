package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.FlightService;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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
	public List<Flight> findFlightsByUser( UUID user ) {
		return stateRetrieving.findFlightsByUser( user );
	}

	@Override
	public void upsert( FlightUpsertRequest request ) {
		User pilot = stateRetrieving.findUserAccount( request.pilot() ).orElse( null );
		User observer = stateRetrieving.findUserAccount( request.observer() ).orElse( null );
		Aircraft aircraft = stateRetrieving.findAircraft( request.aircraft() ).orElse( null );
		Set<Battery> batteries = request.batteries().stream().map( id -> stateRetrieving.findBattery( id ).orElse( null ) ).filter( Objects::nonNull ).collect( Collectors.toSet() );

		// Convert request to a core flight object
		Flight flight = new Flight();
		flight.id( request.id() );
		flight.pilot( pilot );
		flight.unlistedPilot( request.unlistedPilot() );
		flight.observer( observer );
		flight.unlistedObserver( request.unlistedObserver() );
		flight.aircraft( aircraft );
		flight.batteries( batteries );
		flight.timestamp( request.timestamp() );
		flight.duration( request.duration() );
		flight.notes( request.notes() );

		statePersisting.upsert( flight );
	}

	@Override
	public void remove( Flight flight ) {
		statePersisting.remove( flight );
	}

	@Override
	public int getPilotFlightCount( UUID user ) {
		return stateRetrieving.getPilotFlightCount( user );
	}

	@Override
	public long getPilotFlightTime( UUID user ) {
		return stateRetrieving.getPilotFlightTime( user );
	}

}
