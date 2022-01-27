package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Group {

	UUID id = UUID.randomUUID();

	GroupType type;

	String name;

	// This is a user account id
	UUID owner;

}
