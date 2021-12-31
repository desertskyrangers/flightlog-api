package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.port.UserManagement;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserAccountService implements UserManagement {

	@Override
	public Set<UserCredential> find() {
		return Set.of();
	}

	@Override
	public UserCredential find( UUID id ) {
		return null;
	}

	@Override
	public Optional<UserAccount> findByUsername( String username ) {
		// TODO Look up the credentials by username, get the associated user account
		return Optional.empty();
	}

	@Override
	public UserCredential update( UserCredential account ) {
		return null;
	}

	@Override
	public void delete( UserCredential account ) {

	}
}
