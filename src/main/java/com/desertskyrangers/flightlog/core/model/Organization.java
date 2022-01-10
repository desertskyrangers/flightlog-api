package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Organization {

	UUID id = UUID.randomUUID();

	String name;

	// This is a user account id
	UUID owner;

}
