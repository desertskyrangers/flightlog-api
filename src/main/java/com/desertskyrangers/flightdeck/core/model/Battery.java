package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Battery {

	private UUID id = UUID.randomUUID();

	private String name;

	private String make;

	private String model;

	private String connector;

	private BatteryStatus status;

	private BatteryType type;

	private int cells;

	private int cycles;

	private int capacity;

	private int chargeRating;

	private int dischargeRating;

	private UUID owner;

	private OwnerType ownerType;

}
