package com.desertskyrangers.flightdeck.core.model;

public enum OrganizationType {

	CLUB( "member" ),
	COMPANY( "employee" ),
	GROUP( "member" );

	private String memberTitle;

	OrganizationType( String memberTitle ) {
		this.memberTitle = memberTitle;
	}

	public String getMemberTitle() {
		return memberTitle;
	}

}
