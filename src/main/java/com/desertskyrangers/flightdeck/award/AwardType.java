package com.desertskyrangers.flightdeck.award;

import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.User;

/**
 * The super class of all award types.
 */
public class AwardType {

	public boolean meetsRequirements( Flight flight ) {
		return false;
	}

	public boolean meetsRequirements( User user) {
		return false;
	}

}
