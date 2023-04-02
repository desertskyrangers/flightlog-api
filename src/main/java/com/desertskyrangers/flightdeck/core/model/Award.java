package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Accessors( chain = true )
public class Award {

	private UUID id;

	private Type type;

	// private Category category;

	// Presenter?
	//  Could be a group, person or none?
	private Actor presenter;

	// Recipient
	//  Could be a group, person or none?
	private Actor recipient;

	// The date the award was earned
	// In the case of flight awards, this would be the same date as the flight
	private Date earnedDate;

	// Description
	// "In recognition for..."
	private String description;

	private Map<String, String> data;

	// -- Data

	// The flight that accomplished the recognition of the award
	// What if we want to recognize birthday as an award???
	// Or other non-flight related accomplishments???
	// Should there be a subclass FlightAward?
	// What about flight records? Should that be FlightRecordAward?

	// Recognition - this would be what the recipient is being honored for
	// There could be a lot of recognitions, do they all have to be official?
	// Should official recognitions be an enumeration or configuration list?

	// Categories
	// - Flight records (personal, group, app?)
	//   - Time, count, speed, height, distance?
	// - Milestones (first flight, first solo, flight ratings)
	//   - First flight
	//   - Fly the Pattern
	//   - First solo
	//   - First loop
	//   - First roll
	//   - First maiden
	// - Achievements[flight and lifetime] (total hours, total flights)
	//   - Lifetime flight time
	//   - Lifetime flight count
	//   - Daily flight time
	//   - Daily flight count
	//   - Single flight time
	//   - Maiden flight count
	// - Easter Eggs
	//   - PI - Flight time is 3:14
	//   - PI Date - Flight took place on PI day 3/14
	// - Service milestones
	//   - Group positions
	//   - Special service award
	// - Special(video, night flight, automated, fpv, cargo drop)

	public enum Type {
		BADGE,
		MEDAL,
		RIBBON,
		TROPHY
	}

	public Award() {
		data = new ConcurrentHashMap<>();
	}

	public void put( String key, String value ) {
		if( value != null ) {
			data.put( key, value );
		} else {
			data.remove( key );
		}
	}

	public String get( String key ) {
		return getOrDefault( key, null );
	}

	@SuppressWarnings( "SameParameterValue" )
	String getOrDefault( String key, String defaultValue ) {
		return data.getOrDefault( key, defaultValue );
	}

}
