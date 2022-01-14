package com.desertskyrangers.flightdeck.util;

import java.util.UUID;

public class Uuid {

	@SuppressWarnings( "ResultOfMethodCallIgnored" )
	public static boolean isValid( String string ) {
		if( string == null ) return false;
		try {
			UUID.fromString( string );
			return true;
		} catch( IllegalArgumentException exception ) {
			return false;
		}
	}

	public static boolean isNotValid( String string ) {
		return !isValid( string );
	}

}
