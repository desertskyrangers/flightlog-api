package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Accessors( fluent = true )
@Slf4j
public class SmsMessage {

	private Map<String, String> recipients;

	private String message;

	public SmsMessage recipient( String address, String recipient ) {
		recipients( Map.of( address, recipient ) );
		return this;
	}

	public SmsMessage recipients( Map<String, String> recipients ) {
		if( this.recipients == null ) this.recipients = new ConcurrentHashMap<>();
		this.recipients.putAll( recipients );
		return this;
	}

}
