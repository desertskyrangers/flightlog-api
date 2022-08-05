package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Battery;
import com.desertskyrangers.flightdeck.core.model.BatteryStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BatteryServices {

	Optional<Battery> find( UUID id );

	List<Battery> findByOwner( UUID owner );

	Page<Battery> findPageByOwner( UUID owner, int page, int size );

	Page<Battery> findPageByOwnerAndStatus( UUID owner, Set<BatteryStatus> status, int page, int size );

	Battery upsert( Battery battery );

	void remove( Battery battery );

	Battery updateFlightData( Battery battery );
}
