package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserCredentials;
import com.desertskyrangers.flightlog.port.UserManagement;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserAccountService implements UserManagement {

	@Override
	public Set<UserCredentials> find() {
		return Set.of();
	}

	@Override
	public UserCredentials find( UUID id ) {
		return null;
	}

	@Override
	public UserCredentials update( UserCredentials account ) {
		return null;
	}

	@Override
	public void delete( UserCredentials account ) {

	}
}
