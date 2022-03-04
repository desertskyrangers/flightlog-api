package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class AircraftStats {

	private UUID id = UUID.randomUUID();

	private String name;

	private AircraftType type;

	private long lastFlightTimestamp = -1;

	private int flightCount;

	private long flightTime;

}
