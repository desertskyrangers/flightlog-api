package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import com.desertskyrangers.flightdeck.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class DashboardService extends CommonDashboardService<Dashboard> implements DashboardServices {

	public DashboardService( AircraftServices aircraftServices, FlightServices flightServices, UserServices userServices, StateRetrieving stateRetrieving, StatePersisting statePersisting ) {
		super( aircraftServices, flightServices, userServices, stateRetrieving, statePersisting );
		flightServices.setDashboardServices( this );
		userServices.setDashboardServices( this );
	}

	@Override
	public Optional<Dashboard> find( UUID uuid ) {
		return Optional.empty();
	}

	@Override
	public Optional<Dashboard> findByUser( User user ) {
		return getStateRetrieving().findDashboard( user );
	}

	@Override
	public Dashboard upsert( User user, Dashboard dashboard ) {
		return getStatePersisting().upsertDashboard( user, dashboard );
	}

	@Override
	public Dashboard update( User user ) {
		Dashboard dashboard = populate( user, new Dashboard() );
		upsert( user, dashboard );
		return dashboard;
	}

	//@Override
	public String update( Group group ) {
		// Assign a group dashboard id if one does not exist
		if( group.dashboardId() == null ) {
			group.dashboardId(UUID.randomUUID());
			group = getStatePersisting().upsert( group );
		}
		group = getStateRetrieving().findGroup( group.id() ).get();

		// For each user in the group find their pilot flight count and time
		AtomicLong flightCount = new AtomicLong( 0 );
		AtomicLong flightTime = new AtomicLong( 0 );
		group.users().forEach( u -> {
			flightCount.addAndGet( getFlightServices().getPilotFlightCount( u.id() ) );
			flightTime.addAndGet( getFlightServices().getPilotFlightTime( u.id() ) );
		} );

		// Create a map for the dashboard
		Map<String, Object> map = new HashMap<>();
		map.put( "displayName", group.name() );
		map.put( "flightCount", String.valueOf( flightCount ) );
		map.put( "flightTime", String.valueOf( flightTime ) );

		log.warn("Storing {} -> {}", group.dashboardId(), Json.stringify( map ) );

		return getStatePersisting().upsertProjection( group.dashboardId(), Json.stringify( map ) );
	}

}
