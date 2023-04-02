package com.desertskyrangers.flightdeck.award;

import com.desertskyrangers.flightdeck.core.model.Flight;

public class EasterEggPiMinuteSecondFlightTimeAward extends AwardType {

	private static final long PI_DURATION = 3 * 60 + 14;

	public boolean meetsRequirements( Flight flight ) {
		return flight.duration() == PI_DURATION;
	}

}
