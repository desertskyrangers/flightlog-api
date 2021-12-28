package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredentials;
import com.desertskyrangers.flightlog.port.UserManagement;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
	public Optional<UserAccount> findByUsername( String username ) {
		// FIXME Look up the credentials by username, get the associated user account
		return Optional.empty();
	}

	@Override
	public UserCredentials update( UserCredentials account ) {
		return null;
	}

	@Override
	public void delete( UserCredentials account ) {

	}
}
