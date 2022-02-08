package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Prefs {

	private UUID id = UUID.randomUUID();

	private Map<String, Object> data;

}
