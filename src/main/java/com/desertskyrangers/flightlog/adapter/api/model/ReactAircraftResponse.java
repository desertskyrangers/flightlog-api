package com.desertskyrangers.flightlog.adapter.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( chain = true )
public class ReactAircraftResponse {

	private ReactAircraft aircraft;

	private List<String> messages;

}
