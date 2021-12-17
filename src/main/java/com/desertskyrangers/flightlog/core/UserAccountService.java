package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.port.UserManagement;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserAccountService implements UserManagement {

	@Override
	public Set<UserAccount> find() {
		return Set.of();
	}

	@Override
	public UserAccount find( UUID id ) {
		return null;
	}

	@Override
	public UserAccount update( UserAccount account ) {
		return null;
	}

	@Override
	public void delete( UserAccount account ) {

	}
}
