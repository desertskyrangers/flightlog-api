package com.desertskyrangers.flightdeck.award;

import java.util.Set;

public class Awards {

	private static final Set<AwardType> types;

	static {
		types = Set.of( new EasterEggPiSecondsFlightTimeAward(), new EasterEggPiMinuteSecondFlightTimeAward() );
	}

	public static Set<AwardType> getAwardTypes() {
		return types;
	}

}
