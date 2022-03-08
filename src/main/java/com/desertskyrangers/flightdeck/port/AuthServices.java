package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Verification;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public interface AuthServices {

	@Scheduled( fixedRate = 1, timeUnit = TimeUnit.MINUTES )
	void cleanupExpiredVerificationsAndAccounts();

	List<String> requestUserRecover( String username );

	List<String> requestUserReset( Verification verification, String password );

	List<String> requestUserRegister( String username, String email, String password, UUID verifyId );

	List<String> requestUserVerifyResend( UUID id );

	List<String> requestUserVerify( Verification verification );

}
