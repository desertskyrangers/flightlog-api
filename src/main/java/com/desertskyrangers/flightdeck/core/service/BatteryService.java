package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Battery;
import com.desertskyrangers.flightdeck.port.BatteryServices;
import com.desertskyrangers.flightdeck.port.FlightServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BatteryService implements BatteryServices {

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private final FlightServices flightServices;

	public BatteryService( StatePersisting statePersisting, StateRetrieving stateRetrieving, FlightServices flightServices ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
		this.flightServices = flightServices;

		this.flightServices.setBatteryServices( this );
	}

	@Override
	public Optional<Battery> find( UUID id ) {
		return stateRetrieving.findBattery( id );
	}

	@Override
	public List<Battery> findByOwner( UUID owner ) {
		return stateRetrieving.findBatteriesByOwner( owner );
	}

	@Override
	public Battery upsert( Battery battery ) {
		// This logic handles the calculation of initial cycles so that the user
		// does not need to be aware of it. This allows the user to set the cycles
		// without worrying about the flights the battery was used on.
		int flightCycles = flightServices.getBatteryFlightCount( battery );
		return statePersisting.upsert( battery.initialCycles( battery.cycles() - flightCycles ) );
	}

	@Override
	public void remove( Battery battery ) {
		statePersisting.remove( battery );
	}

	@Override
	public Battery updateCycleCount( Battery battery ) {
		Optional<Battery> optional = stateRetrieving.findBattery( battery.id() );
		int initialCycles = optional.map( Battery::initialCycles ).orElse( 0 );
		return upsert( battery.cycles( initialCycles + flightServices.getBatteryFlightCount( battery ) ) );
	}

}
