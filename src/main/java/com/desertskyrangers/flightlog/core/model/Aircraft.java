package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Aircraft {

	private UUID id;

	private String name;

	private AircraftType type;

	private String make;

	private String model;

	private AircraftStatus status;

	private UUID owner;

	private AircraftOwnerType ownerType;

}
