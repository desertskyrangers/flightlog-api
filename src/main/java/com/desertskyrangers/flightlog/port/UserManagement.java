package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;

import java.util.Set;
import java.util.UUID;

public interface UserManagement {

	// TODO Should these methods use the Result pattern?

	Set<UserAccount> find();

	UserAccount find( UUID id );

	UserAccount update( UserAccount user );

	void delete( UserAccount user );

}
