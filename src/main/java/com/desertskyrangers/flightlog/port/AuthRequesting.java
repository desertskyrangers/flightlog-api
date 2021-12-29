package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.Verification;

import java.util.List;

public interface AuthRequesting {

	void requestUserAccountRegister( UserAccount account, UserCredential credentials, Verification verification );

	List<String> requestUserVerify( Verification verification );

}
