package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Verification;

import java.util.List;
import java.util.UUID;

public interface AuthServices {

	List<String> requestUserRecover( String username );

	List<String> requestUserReset( Verification verification, String password );

	List<String> requestUserRegister( String username, String email, String password, UUID verifyId );

	List<String> requestUserVerifyResend( UUID id );

	List<String> requestUserVerify( Verification verification );

}
