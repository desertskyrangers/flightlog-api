package com.desertskyrangers.flightdeck.core.model;

public enum AircraftType {

	FIXEDWING( "Fixed Wing" ),
	HELICOPTER( "Helicopter" ),
	MULTIROTOR( "Multi-rotor" ),
	OTHER( "Other" );

	private final String name;

	AircraftType( String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static boolean isValid( String string ) {
		try {
			valueOf( string.toUpperCase() );
			return true;
		} catch( IllegalArgumentException exception ) {
			return false;
		}
	}

	public static boolean isNotValid( String string ) {
		return !isValid( string );
	}

}
