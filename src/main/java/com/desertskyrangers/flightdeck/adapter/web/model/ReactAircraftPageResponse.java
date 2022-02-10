package com.desertskyrangers.flightdeck.adapter.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( chain = true )
public class ReactAircraftPageResponse {

	private List<ReactAircraft> aircraft;

	private List<String> messages;

}
