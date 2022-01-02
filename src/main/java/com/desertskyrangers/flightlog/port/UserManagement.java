package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserToken;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserManagement {

	// TODO Should these methods use the Result pattern?

	Set<UserToken> find();

	UserToken find( UUID id );

	Optional<UserAccount> findByUsername( String username );

	UserToken update( UserToken user );

	void delete( UserToken user );

}
