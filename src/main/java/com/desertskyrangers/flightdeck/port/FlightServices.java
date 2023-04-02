package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlightServices {

	void setAircraftServices( AircraftServices aircraftServices );

	void setBatteryServices( BatteryServices batteryServices );

	void setDashboardServices( DashboardServices dashboardServices );

	List<Flight> findAll();

	Optional<Flight> find( UUID id );

	List<Flight> findByPilot( User pilot );

	Page<Flight> findFlightsByUser( User user, int page, int size );

	Flight upsert( FlightUpsertRequest flight );

	Flight upsert( Flight flight );

	void remove( Flight flight );

	int getPilotFlightCount( User user );

	long getPilotFlightTime( User user );

	int getObserverFlightCount( User user );

	long getObserverFlightTime( User user );

	Optional<Flight> getLastAircraftFlight( Aircraft aircraft );

	Optional<Flight> getLastPilotFlight( User pilot );

	int getAircraftFlightCount( Aircraft aircraft );

	long getAircraftFlightTime( Aircraft aircraft );

	int getBatteryFlightCount( Battery battery );

	long getBatteryFlightTime( Battery battery );

	Optional<Flight> getFlightWithLongestTime( User user );

	Optional<Flight> getFlightWithLongestTime( Aircraft user );

}
