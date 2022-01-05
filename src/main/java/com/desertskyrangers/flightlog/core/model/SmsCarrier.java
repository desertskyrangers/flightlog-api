package com.desertskyrangers.flightlog.core.model;

public enum SmsCarrier {
	ATT( "AT&T", "txt.att.net", "mms.att.net" ),
	SPRINT( "Sprint", "messaging.sprintpcs.com", "pm.sprint.com" ),
	TMOBILE( "T-Mobile", "tmomail.net", "tmomail.net" ),
	VERIZON( "Verizon", "vtext.com", "vzwpix.com" );

	private final String title;

	private final String smsGateway;

	private final String mmsGateway;

	SmsCarrier( String title, String smsGateway, String mmsGateway ) {
		this.title = title;
		this.smsGateway = smsGateway;
		this.mmsGateway = mmsGateway;
	}

	public String getTitle() {
		return title;
	}

	public String getSmsGateway() {
		return smsGateway;
	}

	public String getMmsGateway() {
		return mmsGateway;
	}

}
