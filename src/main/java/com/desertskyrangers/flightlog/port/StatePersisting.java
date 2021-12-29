package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.Verification;

public interface StatePersisting {

	void upsert( UserAccount account );

	void remove( UserAccount account );

	void upsert( Verification verification );

	void remove( Verification verification );

}
