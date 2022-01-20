package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

	// TODO Should these methods use the Result pattern?

	List<User> find();

	Optional<User> find( UUID id );

	Optional<User> findByPrincipal( String username );

	void upsert( User user );

	void remove( User user );

	Optional<User> findVerificationUser( UUID verificationId );

}
