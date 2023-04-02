package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Award;
import com.desertskyrangers.flightdeck.core.model.Flight;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AwardServices {

	Page<Award> getAwards( UUID id, int page, int size );

	void checkForAwards( Flight flight );

}
