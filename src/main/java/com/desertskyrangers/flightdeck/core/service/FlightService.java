package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlightService implements FlightServices {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private AircraftServices aircraftServices;

	private BatteryServices batteryServices;

	private DashboardServices dashboardServices;

	private static final Map<String, Integer> times = Map.of( "month", 30, "week", 7, "day", 1 );

	private final ThreadLocal<Long> start = new ThreadLocal<>();

	public FlightService( StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	public void setAircraftServices( AircraftServices aircraftServices ) {
		this.aircraftServices = aircraftServices;
	}

	public void setBatteryServices( BatteryServices batteryServices ) {
		this.batteryServices = batteryServices;
	}

	public void setDashboardServices( DashboardServices dashboardServices ) {
		this.dashboardServices = dashboardServices;
	}

	@Override
	public List<Flight> findAll() {
		return stateRetrieving.findAllFlights();
	}

	@Override
	public Optional<Flight> find( UUID id ) {
		return stateRetrieving.findFlight( id );
	}

	@Override
	public List<Flight> findByPilot( User pilot ) {
		return stateRetrieving.findFlightsByPilot( pilot.id() );
	}

	@Override
	public Page<Flight> findFlightsByUser( User user, int page, int size ) {
		boolean showObserverFlights = stateRetrieving.isPreferenceSetTo( user, PreferenceKey.SHOW_OBSERVER_FLIGHTS, "true" );
		boolean showOwnerFlights = stateRetrieving.isPreferenceSetTo( user, PreferenceKey.SHOW_OWNER_FLIGHTS, "true" );

		User observer = showObserverFlights ? user : null;
		User owner = showOwnerFlights ? user : null;
		return stateRetrieving.findFlightsByPilotObserverOwner( user, observer, owner, page, size );
	}

	@Override
	public Flight upsert( FlightUpsertRequest request ) {
		User pilot = stateRetrieving.findUser( request.pilot() ).orElse( null );
		User observer = stateRetrieving.findUser( request.observer() ).orElse( null );
		Aircraft aircraft = stateRetrieving.findAircraft( request.aircraft() ).orElse( null );
		Set<Battery> batteries = request.batteries().stream().map( id -> stateRetrieving.findBattery( id ).orElse( null ) ).filter( Objects::nonNull ).collect( Collectors.toSet() );
		Location location = request.location() == null ? null : stateRetrieving.findLocation( request.location() ).orElse( null );

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
		flight.location( location );
		flight.latitude( request.latitude() );
		flight.longitude( request.longitude() );
		flight.altitude( request.altitude() );
		flight.notes( request.notes() );

		return upsert( flight );
	}

	private long now() {
		return System.currentTimeMillis();
	}

	@Override
	public Flight upsert( Flight flight ) {
		start.set( now() );
		Optional<Flight> prior = stateRetrieving.findFlight( flight.id() );
		log.info( "Time to retrieve prior flight {}", now() - start.get() );
		statePersisting.upsert( flight );
		log.info( "Time to update current flight {}", now() - start.get() );
		updateMetrics( flight, prior );
		return flight;
	}

	@Override
	public void remove( Flight flight ) {
		statePersisting.remove( flight );
		updateMetrics( flight );
	}

	@Override
	public int getPilotFlightCount( User user ) {
		return stateRetrieving.getPilotFlightCount( user );
	}

	@Override
	public long getPilotFlightTime( User user ) {
		return stateRetrieving.getPilotFlightTime( user );
	}

	@Override
	public int getObserverFlightCount( User user ) {
		return stateRetrieving.getObserverFlightCount( user );
	}

	@Override
	public long getObserverFlightTime( User user ) {
		return stateRetrieving.getObserverFlightTime( user );
	}

	@Override
	public Optional<Flight> getLastAircraftFlight( Aircraft aircraft ) {
		return stateRetrieving.getLastAircraftFlight( aircraft );
	}

	@Override
	public Optional<Flight> getLastPilotFlight( User pilot ) {
		return stateRetrieving.getLastPilotFlight( pilot );
	}

	@Override
	public int getAircraftFlightCount( Aircraft aircraft ) {
		return stateRetrieving.getAircraftFlightCount( aircraft );
	}

	@Override
	public long getAircraftFlightTime( Aircraft aircraft ) {
		return stateRetrieving.getAircraftFlightTime( aircraft );
	}

	@Override
	public int getBatteryFlightCount( Battery battery ) {
		return stateRetrieving.getBatteryFlightCount( battery );
	}

	@Override
	public long getBatteryFlightTime( Battery battery ) {
		return stateRetrieving.getBatteryFlightTime( battery );
	}

	@Override
	public Optional<Flight> getFlightWithLongestTime( User user ) {
		return stateRetrieving.getFlightWithLongestTime( user );
	}

	@Override
	public Optional<Flight> getFlightWithLongestTime( Aircraft aircraft ) {
		return stateRetrieving.getFlightWithLongestTime( aircraft );
	}

	private Set<Flight> getFlightsByTime( User user, boolean observer, boolean owner, long time ) {
		Set<Flight> flights = new HashSet<>( stateRetrieving.findFlightsByPilotAndTimestampAfter( user.id(), time ) );
		if( observer ) flights.addAll( stateRetrieving.findFlightsByObserverAndTimestampAfter( user.id(), time ) );
		if( owner ) flights.addAll( stateRetrieving.findFlightsByOwnerAndTimestampAfter( user.id(), time ) );
		return flights;
	}

	private Set<Flight> getFlightsByCount( User user, boolean observer, boolean owner, int count ) {
		Set<Flight> flights = new HashSet<>( stateRetrieving.findFlightsByPilotAndCount( user.id(), count ) );
		if( observer ) flights.addAll( stateRetrieving.findFlightsByObserverAndCount( user.id(), count ) );
		if( owner ) flights.addAll( stateRetrieving.findFlightsByOwnerAndCount( user.id(), count ) );
		return flights;
	}

	@Async
	public CompletableFuture<Void> updateMetrics( Flight flight ) {
		return updateMetrics( flight, Optional.empty() );
	}

	@Async
	public CompletableFuture<Void> updateMetrics( Flight flight, Optional<Flight> prior ) {
		// If the aircraft was changed, the old aircraft data also needs to be updated
		prior.ifPresent( value -> {
			aircraftServices.updateFlightData( value.aircraft() );
			log.info( "Time to update prior flight data {}", now() - start.get() );
		} );

		// Update aircraft flight data
		aircraftServices.updateFlightData( flight.aircraft() );
		log.info( "Time to update current flight data {}", now() - start.get() );

		// Update battery flight data
		flight.batteries().forEach( b -> batteryServices.updateFlightData( b ) );
		log.info( "Time to update current battery data {}", now() - start.get() );

		// Update dashboards
		updateDashboards( flight );
		log.info( "Time to update dashboards {}", now() - start.get() );

		return CompletableFuture.completedFuture( null );
	}

	private void updateDashboards( Flight flight ) {
		// Collect the users involved
		Set<User> users = new HashSet<>();
		users.add( flight.pilot() );
		users.add( flight.observer() );
		if( flight.aircraft().ownerType() == OwnerType.USER ) stateRetrieving.findUser( flight.aircraft().owner() ).ifPresent( users::add );

		// Collect the groups involved
		Set<Group> groups = users.stream().flatMap( u -> u.groups().stream() ).collect( Collectors.toSet() );

		// For each user update the dashboard
		users.forEach( dashboardServices::update );

		// Group dashboards
		groups.forEach( dashboardServices::update );
	}

}
