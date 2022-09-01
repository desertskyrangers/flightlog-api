package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
@Accessors( fluent = true )
@Slf4j
public class SmsMessage {

	private Set<String> numbers;

	private String content;

	public SmsMessage recipient( String number ) {
		recipients( Set.of( number ) );
		return this;
	}

	public SmsMessage recipients( Set<String> numbers ) {
		if( this.numbers == null ) this.numbers = new CopyOnWriteArraySet<>();
		this.numbers.addAll( numbers );
		return this;
	}

}
