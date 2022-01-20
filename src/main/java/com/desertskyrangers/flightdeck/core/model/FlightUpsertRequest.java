package com.desertskyrangers.flightdeck.core.model;

import com.desertskyrangers.flightdeck.core.model.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors( fluent = true )
public class FlightUpsertRequest {

	private User user;

	private UUID id;

	private UUID pilot;

	private String unlistedPilot;

	private UUID observer;

	private String unlistedObserver;

	private UUID aircraft;

	private List<UUID> batteries;

	private long timestamp;

	private int duration;

	private String notes;

}
