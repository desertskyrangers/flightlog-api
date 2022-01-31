package com.desertskyrangers.flightdeck.core.model;

public enum GroupType {

	CLUB( "Club", "member" ),
	COMPANY( "Company", "employee" ),
	GROUP( "Group", "member" );

	private final String name;

	private final String memberTitle;

	GroupType( String name, String memberTitle ) {
		this.name = name;
		this.memberTitle = memberTitle;
	}

	public String getName() {
		return name;
	}

	public String getMemberTitle() {
		return memberTitle;
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
