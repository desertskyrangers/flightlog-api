package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserProfile;
import com.desertskyrangers.flightlog.core.model.Verification;

public interface StatePersisting {

	void upsert( UserAccount account );

	void upsert( UserProfile profile );

	void upsert( Verification verification );

}
