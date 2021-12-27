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

	public static String lpad( String text, int size ) {
		return String.format( "%1$" + size + "s", text );
	}

	public static String lpad( String text, int size, char pad ) {
		return lpad( text, size ).replace( ' ', pad );
	}

	public static String rpad( String text, int size ) {
		return String.format( "%1$-" + size + "s", text );
	}

	public static String rpad( String text, int size, char pad ) {
		return rpad( text, size ).replace( ' ', pad );
	}

}
