package com.desertskyrangers.flightlog.core.model;

public enum AircraftType {

	FIXEDWING( "Fixed Wing" ),
	HELICOPTER( "Helicopter" ),
	MULTIROTOR( "Multi-rotor" ),
	OTHER( "Other" );

	private final String name;

	AircraftType( String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
