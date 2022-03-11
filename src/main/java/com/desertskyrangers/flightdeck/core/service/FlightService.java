package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlightService implements FlightServices {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private AircraftServices aircraftServices;

	private BatteryServices batteryServices;

	private DashboardServices dashboardServices;

	private PublicDashboardServices publicDashboardServices;

	private static final Map<String, Integer> times = Map.of( "month", 30, "week", 7, "day", 1 );

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

	public void setPublicDashboardServices( PublicDashboardServices publicDashboardServices ) {
		this.publicDashboardServices = publicDashboardServices;
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
	public List<Flight> findFlightsByUser( User user ) {
		boolean showObserverFlights = stateRetrieving.isPreferenceSetTo( user, PreferenceKey.SHOW_OBSERVER_FLIGHTS, "true" );
		boolean showOwnerFlights = stateRetrieving.isPreferenceSetTo( user, PreferenceKey.SHOW_OWNER_FLIGHTS, "true" );
		String view = stateRetrieving.getPreference( user, PreferenceKey.FLIGHT_LIST_VIEW, "10" );

		int count;
		try {
			count = Integer.parseInt( view );
		} catch( NumberFormatException exception ) {
			count = -1;
		}

		Set<Flight> flights = new HashSet<>();
		if( count < 0 ) {
			int days = times.get( view );
			long after = System.currentTimeMillis() - TimeUnit.DAYS.toMillis( days );
			flights.addAll( getFlightsByTime( user, showObserverFlights, showOwnerFlights, after ) );
		} else {
			flights.addAll( getFlightsByCount( user, showObserverFlights, showOwnerFlights, count ) );
		}

		List<Flight> orderedFlights = new ArrayList<>( flights );
		if( count >= 0 ) orderedFlights = orderedFlights.subList( 0, Math.min( count, orderedFlights.size() ) );
		orderedFlights.sort( new FlightTimestampComparator().reversed() );

		return orderedFlights;
	}

	@Override
	public Flight upsert( FlightUpsertRequest request ) {
		User pilot = stateRetrieving.findUser( request.pilot() ).orElse( null );
		User observer = stateRetrieving.findUser( request.observer() ).orElse( null );
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

		return upsert( flight );
	}

	@Override
	public Flight upsert( Flight flight ) {
		statePersisting.upsert( flight );
		updateMetrics( flight );
		return flight;
	}

	@Override
	public void remove( Flight flight ) {
		statePersisting.remove( flight );
		updateMetrics( flight );
	}

	@Override
	public int getPilotFlightCount( UUID user ) {
		return stateRetrieving.getPilotFlightCount( user );
	}

	@Override
	public long getPilotFlightTime( UUID user ) {
		return stateRetrieving.getPilotFlightTime( user );
	}

	@Override
	public int getObserverFlightCount( UUID user ) {
		return stateRetrieving.getObserverFlightCount( user );
	}

	@Override
	public long getObserverFlightTime( UUID user ) {
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
	public int getBatteryFlightCount( Battery aircraft ) {
		return stateRetrieving.getBatteryFlightCount( aircraft );
	}

	@Override
	public long getBatteryFlightTime( Battery aircraft ) {
		return stateRetrieving.getBatteryFlightTime( aircraft );
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

	private void updateMetrics( Flight flight ) {
		// Update aircraft flight data
		aircraftServices.updateFlightData( flight.aircraft() );

		// Update battery flight data
		flight.batteries().forEach( b -> batteryServices.updateFlightData( b ) );

		// Update private dashboards
		updateDashboard( flight, dashboardServices );

		// Update public dashboards
		updateDashboard( flight, publicDashboardServices );
	}

	private void updateDashboard( Flight flight, CommonDashboard<?> dashboard ) {
		// Pilot dashboard
		dashboard.update( flight.pilot() );

		// Observer dashboard
		if( !Objects.equals( flight.pilot(), flight.observer() ) ) dashboard.update( flight.observer() );

		// Owner dashboard
		if( flight.aircraft().ownerType() == OwnerType.USER && !Objects.equals( flight.pilot().id(), flight.aircraft().owner() ) ) {
			stateRetrieving.findUser( flight.aircraft().owner() ).ifPresent( dashboard::update );
		}
	}

}
