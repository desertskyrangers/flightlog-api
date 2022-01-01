package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.Verification;

import java.util.List;
import java.util.UUID;

public interface AuthRequesting {

	List<String> requestUserRegister( UserAccount account, UserCredential credentials, Verification verification );

	List<String> requestUserVerify( Verification verification );

	@Deprecated
	UserCredential getUserCredential( UUID userId );
}
