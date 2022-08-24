package com.desertskyrangers.flightdeck;

import com.desertskyrangers.flightdeck.core.service.DashboardService;
import com.desertskyrangers.flightdeck.port.AircraftServices;
import com.desertskyrangers.flightdeck.port.BatteryServices;
import com.desertskyrangers.flightdeck.port.DashboardServices;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DataRefresh implements Runnable {

	private final AircraftServices aircraftServices;

	private final BatteryServices batteryServices;

	private final DashboardServices dashboardServices;

	private final StateRetrieving stateRetrieving;

	public DataRefresh( AircraftServices aircraftServices, BatteryServices batteryServices, DashboardServices dashboardServices, StateRetrieving stateRetrieving ) {
		this.aircraftServices = aircraftServices;
		this.batteryServices = batteryServices;
		this.dashboardServices = dashboardServices;
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public void run() {
		stateRetrieving.findAllAircraft().forEach( aircraftServices::updateFlightData );
		stateRetrieving.findAllBatteries().forEach( batteryServices::updateFlightData );
		stateRetrieving.findAllGroups().forEach( dashboardServices::update );
	}

}
