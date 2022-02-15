package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Flight;

import java.util.Comparator;

public class FlightTimestampComparator implements Comparator<Flight> {

	@Override
	public int compare( Flight o1, Flight o2 ) {
		return Long.compare( o1.timestamp(), o2.timestamp() );
	}

}
