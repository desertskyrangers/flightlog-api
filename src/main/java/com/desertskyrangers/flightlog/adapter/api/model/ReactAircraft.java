package com.desertskyrangers.flightlog.adapter.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
public class ReactAircraft {

	private UUID id;

	private String name;

}

