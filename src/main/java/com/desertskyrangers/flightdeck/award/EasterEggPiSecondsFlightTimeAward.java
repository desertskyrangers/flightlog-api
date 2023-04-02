package com.desertskyrangers.flightdeck.award;

import com.desertskyrangers.flightdeck.core.model.Flight;

public class EasterEggPiSecondsFlightTimeAward extends AwardType {

	public boolean meetsRequirements( Flight flight ) {
		return flight.duration() == 314;
	}

}
