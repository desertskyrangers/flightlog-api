package com.desertskyrangers.flightdeck;

import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataUpdate implements Runnable {

	private final AircraftServices aircraftServices;

	private final BatteryServices batteryServices;

	private final UserServices userServices;

	private final DashboardServices dashboardServices;

	private final StateRetrieving stateRetrieving;

	public DataUpdate( AircraftServices aircraftServices, BatteryServices batteryServices, DashboardServices dashboardServices, UserServices userServices, StateRetrieving stateRetrieving ) {
		this.aircraftServices = aircraftServices;
		this.batteryServices = batteryServices;
		this.dashboardServices = dashboardServices;
		this.userServices = userServices;
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public void run() {
		try {
			stateRetrieving.findAllUsers().forEach( dashboardServices::update );
			stateRetrieving.findAllGroups().forEach( dashboardServices::update );
			stateRetrieving.findAllAircraft().forEach( aircraftServices::updateFlightData );
			stateRetrieving.findAllBatteries().forEach( batteryServices::updateFlightData );

			log.warn( "Dashboards and flight data updated!" );
		} catch( Exception exception ) {
			log.error( "Uh oh!", exception );
		}
	}

}
