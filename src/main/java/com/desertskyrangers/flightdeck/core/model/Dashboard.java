package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( fluent = true )
public class Dashboard {

	private int flightCount;

	private long flightTime;

	private int observerCount;

	private long observerTime;

}
