package com.desertskyrangers.flightdeck.core.model;

public enum PilotRecord {
	TOTAL_FLIGHT_COUNT( "pilotWithHighestTotalFlightCount", "Highest flight count", "The pilot with the highest total flight count across all flight records" ),
	TOTAL_FLIGHT_TIME( "pilotWithHighestTotalFlightTime", "Most flight time", "The pilot with the highest total flight time across all flight records" ),
	LONGEST_SINGLE_FLIGHT_TIME( "pilotWithLongestSingleFlightTime", "Longest flight time", "The pilot with the longest single flight time across all flight records" ),
	MOST_RECENT_FLIGHT( "pilotWithMostRecentFlightDate", "Most recent pilot", "The pilot who flew most recently" );

	private final String key;

	private final String name;

	private final String description;

	PilotRecord( String key, String name, String description ) {
		this.key = key;
		this.name = name;
		this.description = description;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
