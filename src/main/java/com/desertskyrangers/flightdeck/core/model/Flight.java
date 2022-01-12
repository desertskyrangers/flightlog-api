package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Flight {

	private UUID id = UUID.randomUUID();

	private User pilot;

	private User observer;

	private Aircraft aircraft;

	private Set<Battery> batteries;

	private long timestamp;

	private long duration;

	private String notes;

	// departure location
	// arrival location

	// weather ?
	// airspace ?

}
