package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StateRetrievingService implements StateRetrieving {

	@Override
	public UserCredential findUserAccount( UUID id ) {
		// TODO Find entity and convert to bean
		return null;
	}

	@Override
	public UserAccount findUserProfile( UUID id ) {
		// TODO Find entity and convert to bean
		return null;
	}
}
