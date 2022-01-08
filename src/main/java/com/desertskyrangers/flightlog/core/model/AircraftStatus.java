package com.desertskyrangers.flightlog.core.model;

public enum AircraftStatus {

	PREFLIGHT( "Pre-flight" ),
	AIRWORTHY( "Airworthy" ),
	INOPERATIVE( "Inoperative" ),
	DECOMMISSIONED( "Decommissioned" ),
	DESTROYED( "Destroyed" );

	private final String name;

	AircraftStatus( String name ) {
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
