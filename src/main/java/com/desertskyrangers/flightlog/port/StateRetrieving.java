package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.UserAccount;

import java.util.UUID;

public interface StateRetrieving {

	UserCredential findUserAccount( UUID id );

	UserAccount findUserProfile( UUID id );

}
