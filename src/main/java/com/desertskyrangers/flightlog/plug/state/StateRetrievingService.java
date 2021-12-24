package com.desertskyrangers.flightlog.plug.state;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserProfile;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import org.springframework.stereotype.Service;

@Service
public class StateRetrievingService implements StateRetrieving {

	@Override
	public UserAccount findUserAccount( Long id ) {
		// TODO Find entity and convert to bean
		return null;
	}

	@Override
	public UserProfile findUserProfile( Long id ) {
		// TODO Find entity and convert to bean
		return null;
	}
}
