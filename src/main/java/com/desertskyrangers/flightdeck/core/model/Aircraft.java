package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Aircraft {

	private UUID id = UUID.randomUUID();

	private String name;

	private AircraftType type;

	private String make;

	private String model;

	private AircraftStatus status;

	private UUID owner;

	private OwnerType ownerType;

}