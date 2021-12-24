package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserProfile;

public interface StateRetrieving {

	UserAccount findUserAccount( Long id );

	UserProfile findUserProfile( Long id );

}
