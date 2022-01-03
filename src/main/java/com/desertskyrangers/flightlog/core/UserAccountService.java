package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserToken;
import com.desertskyrangers.flightlog.port.StatePersisting;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import com.desertskyrangers.flightlog.port.UserManagement;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserAccountService implements UserManagement {

	private final StateRetrieving stateRetrieving;

	private final StatePersisting statePersisting;

	public UserAccountService( StateRetrieving stateRetrieving, StatePersisting statePersisting ) {
		this.stateRetrieving = stateRetrieving;
		this.statePersisting = statePersisting;
	}

	@Override
	public List<UserAccount> find() {
		return stateRetrieving.findAllUserAccounts();
	}

	@Override
	public Optional<UserAccount> find( UUID id ) {
		return stateRetrieving.findUserAccount( id );
	}

	@Override
	public Optional<UserAccount> findByUsername( String username ) {
		return stateRetrieving.findUserTokenByPrincipal( username ).map( UserToken::userAccount );
	}

	@Override
	public void update( UserAccount account ) {
		statePersisting.upsert( account );
	}

	@Override
	public void delete( UserAccount account ) {
		statePersisting.remove( account );
	}
}
