package com.desertskyrangers.flightdeck.adapter.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( chain = true )
public class ReactFlightPageResponse {

	private List<ReactFlight> flights;

	private List<String> messages;

}
