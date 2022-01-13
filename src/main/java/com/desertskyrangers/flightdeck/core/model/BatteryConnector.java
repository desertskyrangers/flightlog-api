package com.desertskyrangers.flightdeck.core.model;

public enum BatteryConnector {

	XT30( "XT30" ),
	XT60( "XT60" ),
	XT90( "XT90" ),
	DEANS( "Deans T Plug" ),
	DEANS_MINI( "Deans Mini" ),
	EC2( "EC2" ),
	EC3( "EC3" ),
	EC5( "EC5" ),
	JST( "JST" ),
	MOLEX( "Molex" ),
	SERVO( "Servo" ),
	TRX( "TRX" );

	private final String name;

	BatteryConnector( String name ) {
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
