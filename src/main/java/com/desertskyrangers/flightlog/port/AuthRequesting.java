package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.Verification;

public interface AuthRequesting {

	void requestUserAccountSignup( UserAccount account, UserCredential credentials );

	void requestUserVerify( Verification verification );

}
