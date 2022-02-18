package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Dashboard;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.DashboardServices;
import com.desertskyrangers.flightdeck.port.FlightServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class DashboardService implements DashboardServices {

	private final FlightServices flightServices;

	public DashboardService( FlightServices flightServices ) {
		this.flightServices = flightServices;
	}

	@Override
	public Optional<Dashboard> find( UUID uuid ) {
		return Optional.empty();
	}

	@Override
	public Optional<Dashboard> findByUser( User user ) {
		int flightCount = flightServices.getPilotFlightCount( user.id() );
		long flightTime = flightServices.getPilotFlightTime( user.id() );
		return Optional.of( new Dashboard().flightCount( flightCount ).flightTime( flightTime ) );
	}

	@Override
	public Dashboard upsert( User user, Dashboard dashboard ) {
		//statePersisting.upsert( user, dashboard );
		return dashboard;
	}

}
