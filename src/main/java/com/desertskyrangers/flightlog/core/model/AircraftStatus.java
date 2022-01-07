package com.desertskyrangers.flightlog.core.model;

public enum AircraftStatus {

	PREFLIGHT( "Pre-flight" ),
	AIRWORTHY( "Airworthy" ),
	INOPERATIVE( "Inoperative" ),
	DECOMMISSIONED( "Decommissioned" ),
	DESTROYED( "Destroyed" );

	private final String name;

	AircraftStatus( String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
