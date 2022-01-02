package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserToken;
import com.desertskyrangers.flightlog.core.model.Verification;

import java.util.List;
import java.util.UUID;

public interface AuthRequesting {

	List<String> requestUserRegister( UserAccount account, UserToken credentials, Verification verification );

	List<String> requestUserVerifyResend( UUID id );

	List<String> requestUserVerify( Verification verification );

	@Deprecated
	UserToken getUserCredential( UUID userId );
}
