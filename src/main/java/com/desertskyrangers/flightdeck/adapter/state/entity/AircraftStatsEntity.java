package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.AircraftStats;
import com.desertskyrangers.flightdeck.core.model.AircraftType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
public class AircraftStatsEntity {

	private UUID id;

	private String name;

	private String type;

	private int flightCount;

	private long flightTime;

	public static AircraftStatsEntity from( AircraftStats stats ) {
		AircraftStatsEntity entity = new AircraftStatsEntity();

		entity.setId( stats.id() );
		entity.setName( stats.name() );
		entity.setType( stats.type().name().toLowerCase() );
		entity.setFlightCount( stats.flightCount() );
		entity.setFlightTime( stats.flightTime() );

		return entity;
	}

	public static AircraftStats toAircraftStats( AircraftStatsEntity entity ) {
		AircraftStats stats = new AircraftStats();

		stats.id( entity.getId() );
		stats.name( entity.getName() );
		stats.type( AircraftType.valueOf( entity.getType().toUpperCase() ) );
		stats.flightCount( entity.getFlightCount() );
		stats.flightTime( entity.getFlightTime() );

		return stats;
	}

}
