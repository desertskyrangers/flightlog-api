package com.desertskyrangers.flightdeck.util;

import java.util.regex.Pattern;

public class Email {

	private static final Pattern ADDRESS_PATTERN = Pattern.compile(
		"[a-z0-9!#$%&'*+\\\\/=?^_{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\\\/=?^_{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]*[a-z0-9]" );

	public static boolean isValid( String email ) {
		if( email == null ) return false;
		return ADDRESS_PATTERN.matcher( email ).matches();
	}

}
