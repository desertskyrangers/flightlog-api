package com.desertskyrangers.flightdeck.util;

import com.desertskyrangers.flightdeck.core.model.User;

public class Names {

	public static String firstNameAndLastInitial( User user ) {
		if( user == null ) return null;
		String lastNameInitial = String.valueOf( user.lastName() ).substring( 0, 1 );
		return (user.firstName() + " " + lastNameInitial).trim();
	}

}
