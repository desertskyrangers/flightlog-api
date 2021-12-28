package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredentials;
import com.desertskyrangers.flightlog.core.model.Verification;

public interface AuthRequesting {

	void requestUserAccountSignup( UserAccount account, UserCredentials credentials );

	void requestUserVerify( Verification verification );

}
