package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Aircraft {

	UUID id;

	AircraftOwnerType ownerType;

	UUID ownerId;

}
