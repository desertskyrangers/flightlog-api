package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserCredentials;
import com.desertskyrangers.flightlog.core.model.UserAccount;

import java.util.UUID;

public interface StateRetrieving {

	UserCredentials findUserAccount( UUID id );

	UserAccount findUserProfile( UUID id );

}
