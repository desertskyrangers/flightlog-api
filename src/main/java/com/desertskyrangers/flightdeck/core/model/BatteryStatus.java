package com.desertskyrangers.flightdeck.core.model;

public enum BatteryStatus {

	NEW( "New", true ),
	AVAILABLE( "Available", true ),
	DESTROYED( "Destroyed", false );

	private final String name;

	private final boolean airworthy;

	BatteryStatus( String name, boolean airworthy ) {
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
