package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserToken;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserManagement {

	// TODO Should these methods use the Result pattern?

	List<UserAccount> find();

	Optional<UserAccount> find( UUID id );

	Optional<UserAccount> findByUsername( String username );

	void update( UserAccount user );

	void delete( UserAccount user );

}
