package com.desertskyrangers.flightlog.util;

public class Text {

	public static boolean isNull( String text ) {
		return text == null;
	}

	public static boolean isNotNull( String text ) {
		return !isNull( text );
	}

	public static boolean isEmpty( String text ) {
		return isNull( text ) || "".equals( text );
	}

	public static boolean isNotEmpty( String text ) {
		return !isEmpty( text );
	}

	public static boolean isBlank( String text ) {
		return isNull( text ) || isEmpty( text.trim() );
	}

	public static boolean isNotBlank( String text ) {
		return !isBlank( text );
	}

}
