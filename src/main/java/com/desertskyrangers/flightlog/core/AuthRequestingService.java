package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.EmailMessage;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.port.HumanInterface;
import com.desertskyrangers.flightlog.port.StatePersisting;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthRequestingService implements AuthRequesting {

	private final StatePersisting statePersisting;

	private final HumanInterface humanInterface;

	public AuthRequestingService( StatePersisting statePersisting, HumanInterface humanInterface ) {
		this.statePersisting = statePersisting;
		this.humanInterface = humanInterface;
	}

	@Override
	public void requestUserAccountSignup( UserAccount account ) {
		log.info( "Creating account for: " + account.username() );

		// TODO Block repeat attempts to generate an account

		// Generate a new account
		statePersisting.upsert( account );

		// Generate a verification code
		String code = "000000";

		// Generate a verification record
		Verification verification = new Verification();
		verification.userId( account.id() );
		verification.timestamp( System.currentTimeMillis() );
		verification.code( code );
		verification.type( "email" );
		statePersisting.upsert( verification );

		// Send the message to verify the email address
		sendEmailAddressVerificationMessage( account, code );
	}

	@Async
	void sendEmailAddressVerificationMessage( UserAccount account, String code ) {
		String subject = "FlightLog Email Account Verification";
		String link = "https://www.flightlog.desertskyrangers.com/api/auth/verify?id=" + account.id() + "&code=" + code;

		String verificationMessage = generateEmailAddressVerificationMessage( subject, link, code );
		if( verificationMessage == null ) return;

		EmailMessage message = new EmailMessage();
		message.recipient( account.email(), account.username() );
		message.subject( subject );
		message.message( verificationMessage );
		message.isHtml( true );
		humanInterface.email( message );
	}

	private String generateEmailAddressVerificationMessage( String subject, String link, String code ) {
		Map<String, Object> values = new HashMap<>();
		values.put( "subject", subject );
		values.put( "link", link );
		values.put( "code", code );

		PebbleEngine engine = new PebbleEngine.Builder().build();
		PebbleTemplate compiledTemplate = engine.getTemplate( "templates/verify-email.html" );

		StringWriter writer = new StringWriter();
		try {
			compiledTemplate.evaluate( writer, values );
		} catch( IOException exception ) {
			log.error( "Unable to process email template", exception );
		}
		return writer.toString();
	}

}
