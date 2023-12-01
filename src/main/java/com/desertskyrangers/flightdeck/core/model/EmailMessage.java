package com.desertskyrangers.flightdeck.core.model;

import jakarta.mail.internet.InternetAddress;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Accessors( fluent = true )
@Slf4j
public class EmailMessage {

	private Map<String, String> recipients;

	private String subject;

	private String message;

	private boolean isHtml;

	public InternetAddress[] recipients() {
		List<InternetAddress> recipients = new ArrayList<>();
		for( Entry<String, String> e : this.recipients.entrySet() ) {
			try {
				recipients.add( new InternetAddress( e.getKey(), e.getValue() ) );
			} catch( UnsupportedEncodingException exception ) {
				log.warn( "Invalid email address=" + e.getKey() );
			}
		}
		return recipients.toArray( new InternetAddress[ 0 ] );
	}

	public EmailMessage recipient( String address, String recipient ) {
		recipients( Map.of( address, recipient ) );
		return this;
	}

	public EmailMessage recipients( Map<String, String> recipients ) {
		if( this.recipients == null ) this.recipients = new ConcurrentHashMap<>();
		this.recipients.putAll( recipients );
		return this;
	}

	public EmailMessage message( String message ) {
		this.message = message;
		if( message.startsWith( "<!DOCTYPE html" ) ) isHtml = true;
		if( message.startsWith( "<html" ) ) isHtml = true;
		return this;
	}

}
