package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserProfile;

import java.util.UUID;

public interface StateRetrieving {

	UserAccount findUserAccount( UUID id );

	UserProfile findUserProfile( UUID id );

}
