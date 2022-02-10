package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Battery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BatteryServices {

	Optional<Battery> find( UUID id );

	List<Battery> findByOwner( UUID owner );

	void upsert( Battery battery );

	void remove( Battery battery );

}
