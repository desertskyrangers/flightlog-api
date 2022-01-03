package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserToken;
import com.desertskyrangers.flightlog.core.model.Verification;

import java.util.*;

public interface StateRetrieving {

	Optional<UserToken> findUserCredential( UUID id );

	Optional<UserToken> findUserTokenByPrincipal( String username );

	List<UserAccount> findAllUserAccounts();

	Optional<UserAccount> findUserAccount( UUID id );

	List<Verification> findAllVerifications();

	Optional<Verification> findVerification( UUID id );
}
