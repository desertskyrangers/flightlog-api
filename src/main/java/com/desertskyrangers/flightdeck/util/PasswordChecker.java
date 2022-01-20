package com.desertskyrangers.flightdeck.util;

public class PasswordChecker {

	public static boolean isValid( String password ) {
		if( password.length() < 8) return false;
		if( password.length() >= 128 ) return false;
		return true;
	}

	public static boolean isNotValid( String password ) {
		return !isValid( password );
	}

}
