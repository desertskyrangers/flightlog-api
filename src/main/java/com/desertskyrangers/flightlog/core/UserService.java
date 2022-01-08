package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.User;
import com.desertskyrangers.flightlog.core.model.UserToken;
import com.desertskyrangers.flightlog.port.StatePersisting;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import com.desertskyrangers.flightlog.port.UserManagement;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserManagement {

	private final StateRetrieving stateRetrieving;

	private final StatePersisting statePersisting;

	public UserService( StateRetrieving stateRetrieving, StatePersisting statePersisting ) {
		this.stateRetrieving = stateRetrieving;
		this.statePersisting = statePersisting;
	}

	@Override
	public List<User> find() {
		return stateRetrieving.findAllUserAccounts();
	}

	@Override
	public Optional<User> find( UUID id ) {
		return stateRetrieving.findUserAccount( id );
	}

	@Override
	public Optional<User> findByPrincipal( String username ) {
		return stateRetrieving.findUserTokenByPrincipal( username ).map( UserToken::user );
	}

	@Override
	public void upsert( User account ) {
		statePersisting.upsert( account );
	}

	@Override
	public void remove( User account ) {
		statePersisting.remove( account );
	}
}
