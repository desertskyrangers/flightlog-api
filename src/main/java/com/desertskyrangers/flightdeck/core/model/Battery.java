package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Battery {

	public static final int MAX_CYCLES = 200;

	private UUID id = UUID.randomUUID();

	private String name;

	private String make;

	private String model;

	private BatteryConnector connector;

	private String unlistedConnector;

	private BatteryStatus status;

	private BatteryType type;

	private int cells;

	private int initialCycles;

	private int capacity;

	private int dischargeRating;

	private UUID owner;

	private OwnerType ownerType;

	public int life() {
		return (int)((100D * (Battery.MAX_CYCLES - initialCycles()) / Battery.MAX_CYCLES));
	}

}
