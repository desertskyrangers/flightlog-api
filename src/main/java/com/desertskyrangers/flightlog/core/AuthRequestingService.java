package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.EmailMessage;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.port.HumanInterface;
import com.desertskyrangers.flightlog.port.StatePersisting;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuthRequestingService implements AuthRequesting {

	private final StatePersisting statePersisting;

	private final HumanInterface humanInterface;

	public AuthRequestingService( StatePersisting statePersisting, HumanInterface humanInterface ) {
		this.statePersisting = statePersisting;
		this.humanInterface = humanInterface;
	}

	@Override
	public void requestUserAccountSignup( UserAccount account ) {
		System.out.println( "Creating account for: " + account.username() );
		// TODO Block repeat attempts to generate an account

		// Generate a new account
		statePersisting.upsert( account );

		// Send the message to verify the email address
		sendEmailAddressVerificationMessage( account );
	}

	@Async
	void sendEmailAddressVerificationMessage( UserAccount account ) {
		EmailMessage message = new EmailMessage();
		message.recipient( account.email() );
		message.subject( "Desert Sky Rangers Email Account Verification" );
		message.message( generateEmailAddressVerificationMessage() );
		humanInterface.email( message );
	}

	private String generateEmailAddressVerificationMessage() {
		StringBuilder builder = new StringBuilder();

		builder.append( "<html>\n" );
		builder.append( "<head>\n" );
		builder.append( "</head>\n" );
		builder.append( "<body>\n" );
		builder.append( "<h1>Desert Sky Rangers Email Account Verification</h1>\n" );
		builder.append( "<p>Please verify your Desert Sky Rangers account email address by clicking on the following link or pasting it into a browser:</p>\n" );
		builder.append( "<p><a href=\"\">Verification Link</a></p>\n" );
		builder.append( "</body>\n" );
		builder.append( "</html>\n" );

		return builder.toString();
	}

}
