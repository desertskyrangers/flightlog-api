package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserCredentials;

import java.util.Set;
import java.util.UUID;

public interface UserManagement {

	// TODO Should these methods use the Result pattern?

	Set<UserCredentials> find();

	UserCredentials find( UUID id );

	UserCredentials update( UserCredentials user );

	void delete( UserCredentials user );

}
