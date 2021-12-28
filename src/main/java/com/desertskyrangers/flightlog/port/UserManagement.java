package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserManagement {

	// TODO Should these methods use the Result pattern?

	Set<UserCredential> find();

	UserCredential find( UUID id );

	Optional<UserAccount> findByUsername( String username );

	UserCredential update( UserCredential user );

	void delete( UserCredential user );

}
