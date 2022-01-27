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

}
