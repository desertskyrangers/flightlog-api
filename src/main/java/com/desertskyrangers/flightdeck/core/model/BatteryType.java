package com.desertskyrangers.flightdeck.core.model;

public enum BatteryType {

	NICD( "NiCd", 1.2D ),
	LIPO( "LiPo", 3.7D ),
	LIFE( "LiFe", 3.3D ),
	NIMH( "NiMH", 1.2D ),
	NIZN( "NiZn", 1.6D ),
	LEAD( "Lead", 2.0D );

	private final String name;

	private final double voltsPerCell;

	BatteryType( String name, double voltsPerCell ) {
		this.name = name;
		this.voltsPerCell = voltsPerCell;
	}

	public String getName() {
		return name;
	}

	public double getVoltsPerCell() {
		return voltsPerCell;
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
