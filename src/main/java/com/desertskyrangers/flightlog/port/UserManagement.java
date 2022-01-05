package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserManagement {

	// TODO Should these methods use the Result pattern?

	List<UserAccount> find();

	Optional<UserAccount> find( UUID id );

	Optional<UserAccount> findByPrincipal( String username );

	void upsert( UserAccount user );

	void remove( UserAccount user );

}
