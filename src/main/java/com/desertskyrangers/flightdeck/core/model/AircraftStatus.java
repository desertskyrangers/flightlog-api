package com.desertskyrangers.flightdeck.core.model;

public enum AircraftStatus {

	PREFLIGHT( "Pre-flight", true ),
	AIRWORTHY( "Airworthy", true ),
	INOPERATIVE( "Inoperative", false ),
	DECOMMISSIONED( "Decommissioned", false ),
	DESTROYED( "Destroyed", false );

	private final String name;

	private final boolean airworthy;

	AircraftStatus( String name, boolean airworthy ) {
		this.name = name;
		this.airworthy = airworthy;
	}

	public String getName() {
		return name;
	}

	public boolean isAirworthy() {
		return airworthy;
	}

	public static boolean isValid( String string ) {
		try {
			valueOf( string.toUpperCase() );
			return true;
		} catch( NullPointerException | IllegalArgumentException exception ) {
			return false;
		}
	}

	public static boolean isNotValid( String string ) {
		return !isValid( string );
	}

}
