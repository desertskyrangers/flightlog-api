package com.desertskyrangers.flightlog.plug.state;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserProfile;
import com.desertskyrangers.flightlog.port.StatePersisting;
import org.springframework.stereotype.Service;

@Service
public class StatePersistingService implements StatePersisting {

	@Override
	public void upsert( UserAccount account ) {

	}

	@Override
	public void upsert( UserProfile profile ) {

	}
}
