package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.*;

public interface StatePersisting {

	void upsert( Aircraft aircraft );

	void remove( Aircraft aircraft );

	void upsert( Battery battery );

	void remove( Battery battery );

	void upsert( Flight flight );

	void remove( Flight flight );

	void removeAllFlights();

	void upsert( User account );

	void remove( User account );

	void upsert( Verification verification );

	void remove( Verification verification );

}
