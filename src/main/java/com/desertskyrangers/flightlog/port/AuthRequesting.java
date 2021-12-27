package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;

public interface AuthRequesting {

	void requestUserAccountSignup( UserAccount account );

}
