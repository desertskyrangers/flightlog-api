package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( fluent = true )
public class PublicDashboard {

	private String displayName;

	private int flightCount;

	private long flightTime;

	private int observerCount;

	private long observerTime;

	private long lastPilotFlightTimestamp = -1;

	private List<AircraftStats> aircraftStats = List.of();

}
