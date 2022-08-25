package com.desertskyrangers.flightdeck.util;

public enum SmsCarrier {
	ATT( "AT&T", "txt.att.net", "mms.att.net" ),
	GABB( "Gabb", "vtext.com", "vzwpix.com" ),
	SPRINT( "Sprint", "messaging.sprintpcs.com", "pm.sprint.com" ),
	TMOBILE( "T-Mobile", "tmomail.net", "tmomail.net" ),
	VERIZON( "Verizon", "vtext.com", "vzwpix.com" ),
	NONE( "No Carrier", "", "" );

	private final String name;

	private final String smsGateway;

	private final String mmsGateway;

	SmsCarrier( String name, String smsGateway, String mmsGateway ) {
		this.name = name;
		this.smsGateway = smsGateway;
		this.mmsGateway = mmsGateway;
	}

	public String getName() {
		return name;
	}

	public String getSmsGateway() {
		return smsGateway;
	}

	public String getMmsGateway() {
		return mmsGateway;
	}

	public String smsFor( String number ) {
		return number +"@" + getSmsGateway();
	}

	public String mmsFor( String number ) {
		return number +"@" + getMmsGateway();
	}

}
