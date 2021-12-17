package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserProfile;

public interface StatePersisting {

	void upsert( UserAccount account );

	void upsert( UserProfile profile );

}
