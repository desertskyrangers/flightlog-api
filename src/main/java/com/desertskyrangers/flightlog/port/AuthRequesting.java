package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;

public interface AuthRequesting {

	UserAccount requestUserAccountSignup( UserAccount account );

}
