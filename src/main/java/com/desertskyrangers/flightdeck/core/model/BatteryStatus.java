package com.desertskyrangers.flightdeck.core.model;

public enum BatteryStatus {

	NEW( "New" ),
	AVAILABLE( "Available" ),
	DESTROYED( "Destroyed" );

	private final String name;

	BatteryStatus( String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
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
