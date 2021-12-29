package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.Verification;

import java.util.Optional;
import java.util.UUID;

public interface StateRetrieving {

	Optional<UserCredential> findUserCredential( UUID id );

	Optional<UserAccount> findUserAccount( UUID id );

	Optional<Verification> findVerification( UUID id );
}
