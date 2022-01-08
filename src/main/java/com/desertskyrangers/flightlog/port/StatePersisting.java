package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.Aircraft;
import com.desertskyrangers.flightlog.core.model.User;
import com.desertskyrangers.flightlog.core.model.Verification;

public interface StatePersisting {

	void upsert( User account );

	void remove( User account );

	void upsert( Verification verification );

	void remove( Verification verification );

	void upsert( Aircraft aircraft );

	void remove( Aircraft aircraft );

}
