package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.Verification;

import java.util.List;
import java.util.UUID;

public interface AuthRequesting {

	List<String> requestUserRegister( String username, String email, String password, UUID verifyId  );

	List<String> requestUserVerifyResend( UUID id );

	List<String> requestUserVerify( Verification verification );

}
