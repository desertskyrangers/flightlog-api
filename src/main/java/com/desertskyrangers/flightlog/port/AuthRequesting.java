package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.Verification;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AuthRequesting {

	List<String> requestUserRegister( UserAccount account, UserCredential credentials, Verification verification );

	List<String> requestUserVerify( Verification verification );

	Authentication authenticate( UserCredential credential );
}
