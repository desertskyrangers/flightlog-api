package com.desertskyrangers.flightdeck.core.model;

public enum MemberStatus {

	OWNER( "Owner" ),
	ACCEPTED( "Accepted" ),
	INVITED( "Invited" ),
	REQUESTED( "Requested" ),
	REVOKED( "Revoked" );

	private final String name;

	MemberStatus( String name ) {
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
