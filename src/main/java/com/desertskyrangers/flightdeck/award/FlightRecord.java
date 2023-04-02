package com.desertskyrangers.flightdeck.award;

import com.desertskyrangers.flightdeck.core.model.Flight;

import java.util.List;

public class FlightRecord extends AwardType {

	// Flight records have a history of the flights that break the record

	/**
	 * Get the reverse-cron ordered list of flights that break this record
	 *
	 * @return The list of flights
	 */
	public List<Flight> getFlights() {
		return List.of();
	}

}
