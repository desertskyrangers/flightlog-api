package com.desertskyrangers.flightdeck.core.model;

public enum LocationStatus {

	ACTIVE,

	REMOVED;

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
