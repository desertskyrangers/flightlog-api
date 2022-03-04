package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.core.model.AircraftStats;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ReactAircraftStats {

	private String id;

	private String name;

	private String type;

	private Long lastFlightTimestamp;

	private Integer flightCount;

	private Long flightTime;

	public static ReactAircraftStats from( AircraftStats stats ) {
		ReactAircraftStats reactStats = new ReactAircraftStats();

		reactStats.setId( stats.id().toString() );
		reactStats.setName( stats.name() );
		reactStats.setType( stats.type().name().toLowerCase() );
		reactStats.setLastFlightTimestamp( stats.lastFlightTimestamp() );
		reactStats.setFlightCount( stats.flightCount() );
		reactStats.setFlightTime( stats.flightTime() );

		return reactStats;
	}

}
