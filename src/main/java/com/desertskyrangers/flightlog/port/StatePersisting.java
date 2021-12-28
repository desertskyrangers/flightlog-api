package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.Verification;

public interface StatePersisting {

	void upsert( UserCredential account );

	void upsert( UserAccount profile );

	void upsert( Verification verification );

}
