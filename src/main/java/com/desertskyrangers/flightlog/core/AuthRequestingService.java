package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.port.StatePersisting;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuthRequestingService implements AuthRequesting {

	private final StatePersisting statePersisting;

	public AuthRequestingService( StatePersisting statePersisting ) {
		this.statePersisting = statePersisting;
	}

	@Override
	public void requestUserAccountSignup( UserAccount account ) {
		// Generate a new account
		statePersisting.upsert( account );

		// Send the message to verify the email address
		sendEmailAddressVerificationMessage( account );
	}

	@Async
	void sendEmailAddressVerificationMessage( UserAccount account ) {
		// TODO Send verification email
	}

}
