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

	Group upsert( Group group );

	void remove( Group group );

	void removeAllGroups();

	Member upsert( Member member );

	void remove( Member member );

	void removeAllMembers();

	User upsert( User account );

	void remove( User account );

	void upsert( UserToken token );

	Verification upsert( Verification verification );

	Verification remove( Verification verification );

}
