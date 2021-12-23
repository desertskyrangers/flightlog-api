package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
@Accessors( fluent = true )
public class EmailMessage {

	private Set<String> recipients;

	private String subject;

	private String message;

	public Set<String> recipients() {
		return new HashSet<>( recipients );
	}

	public EmailMessage recipient( String recipient ) {
		recipients( Set.of( recipient ) );
		return this;
	}

	public EmailMessage recipients( Set<String> recipients ) {
		if( this.recipients == null ) this.recipients = new CopyOnWriteArraySet<>();
		this.recipients.addAll( recipients );
		return this;
	}

}
