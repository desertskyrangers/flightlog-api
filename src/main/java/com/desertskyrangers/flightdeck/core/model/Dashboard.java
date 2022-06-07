package com.desertskyrangers.flightdeck.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors( fluent = true )
public class Dashboard {

	private int flightCount;

	private long flightTime;

	private int observerCount;

	private long observerTime;

	private long lastPilotFlightTimestamp = -1;

	private List<AircraftStats> aircraftStats = List.of();

}
