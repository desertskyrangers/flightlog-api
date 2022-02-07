package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.adapter.api.model.ReactDashboard;
import com.desertskyrangers.flightdeck.core.model.Dashboard;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.DashboardService;
import com.desertskyrangers.flightdeck.port.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

	private final FlightService flightService;

	public DashboardServiceImpl( FlightService flightService ) {
		this.flightService = flightService;
	}

	@Override
	public Optional<Dashboard> find( UUID uuid ) {
		return Optional.empty();
	}

	@Override
	public Optional<Dashboard> findByUser( User user ) {
		int flightCount = flightService.getPilotFlightCount( user.id() );
		long flightTime = flightService.getPilotFlightTime( user.id() );
		return Optional.of( new Dashboard().flightCount( flightCount ).flightTime( flightTime ) );
	}
}
