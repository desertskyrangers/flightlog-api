package com.desertskyrangers.flightdeck.core.model;

public class Award {

	private Type type;

	// private Category category;

	// Presenter?
	//  Could be a group, person or none?
	private Actor presenter;

	// Recipient
	//  Could be a group, person or none?
	private Actor recipient;

	// The flight that accomplished the recognition of the award
	// What if we want to recognize birthday as an award???
	// Or other non-flight related accomplishments???
	// Should there be a subclass FlightAward?
	// What about flight records? Should that be FlightRecordAward?

	private Flight flight;

	// Recognition - this would be what the recipient is being honored for
	// There could be a lot of recognitions, do they all have to be official?
	// Should official recognitions be an enumeration or configuration list?

	// Categories
	// - Flight records (personal, group, app?)
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
	// - Special(video, night flight, automated, fpv, cargo drop)

	public enum Type {
		BADGE,
		MEDAL,
		RIBBON,
		TROPHY
	}

}




















