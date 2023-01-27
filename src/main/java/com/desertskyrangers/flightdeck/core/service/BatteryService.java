package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Battery;
import com.desertskyrangers.flightdeck.port.BatteryServices;
import com.desertskyrangers.flightdeck.port.FlightServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
	public Page<Battery> findPageByOwner( UUID owner, int page, int size ) {
		return stateRetrieving.findBatteriesPageByOwner( owner, page, size );
	}

	@Override
	public Page<Battery> findPageByOwnerAndStatus( UUID owner, Set<Battery.Status> status, int page, int size ) {
		return stateRetrieving.findBatteriesPageByOwnerAndStatus( owner, status, page, size );
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
	public Battery updateFlightData( Battery battery ) {
		Optional<Battery> optional = stateRetrieving.findBattery( battery.id() );
		if( optional.isEmpty() ) return battery;

		int flightCount = flightServices.getBatteryFlightCount( battery );
		long flightTime = flightServices.getBatteryFlightTime( battery );

		battery = optional.get();
		battery.cycles( battery.initialCycles() + flightCount );
		battery.flightCount( flightCount );
		battery.flightTime( flightTime );

		return upsert( battery );
	}

}
