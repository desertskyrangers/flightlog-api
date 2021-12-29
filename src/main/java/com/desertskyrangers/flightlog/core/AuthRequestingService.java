package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.adapter.api.ApiPath;
import com.desertskyrangers.flightlog.core.model.EmailMessage;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.port.HumanInterface;
import com.desertskyrangers.flightlog.port.StatePersisting;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import com.desertskyrangers.flightlog.util.Text;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Service
@Slf4j
public class AuthRequestingService implements AuthRequesting {

	private static final String VERIFY_ENDPOINT = ApiPath.HOST + ApiPath.AUTH_VERIFY;

	private static final String EMAIL_SUBJECT = "FlightLog Email Account Verification";

	private static final String TEMPLATES_VERIFY_EMAIL_HTML = "templates/verify-email.html";

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private final HumanInterface humanInterface;

	public AuthRequestingService( StatePersisting statePersisting, StateRetrieving stateRetrieving, HumanInterface humanInterface ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
		this.humanInterface = humanInterface;
	}

	@Async
	@Override
	public void requestUserAccountRegister( UserAccount account, UserCredential credentials, Verification verification ) {
		log.info( "Creating account username: " + credentials.username() );

		// TODO Block repeat attempts to generate an account

		// Generate a new account
		account.credentials().add( credentials );
		statePersisting.upsert( account );

		// Generate a verification code
		String code = Text.lpad( String.valueOf( new Random().nextInt( 1000000 ) ), 6, '0' );

		// Generate a verification record
		verification.userId( account.id() );
		verification.code( code );
		verification.type( Verification.EMAIL_VERIFY_TYPE );
		statePersisting.upsert( verification );

		log.warn("verification code: " + verification.code() );

		// Send the message to verify the email address
		sendEmailAddressVerificationMessage( account, credentials, verification );
	}

	@Override
	public List<String> requestUserVerify( Verification verification ) {
		List<String> messages = new ArrayList<>();

		Optional<Verification> optional = stateRetrieving.findVerification( verification.id() );
		if( optional.isPresent() ) {
			Verification storedVerification = optional.get();

			boolean validCode = Objects.equals( storedVerification.code(), verification.code() );
			long duration = verification.timestamp() - storedVerification.timestamp();

			if( !validCode ) messages.add( "Invalid verification code: " + verification.code() );
			if( duration < 0 ) messages.add( "Invalid verification timestamp" );
			if( duration > Verification.CODE_TIMEOUT ) messages.add( "Verification code expired" );

			if( messages.size() == 0 ) {
				stateRetrieving.findUserAccount(storedVerification.userId()).ifPresent( u -> {
					switch( storedVerification.type() ) {
						case Verification.EMAIL_VERIFY_TYPE: {
							setEmailVerified( u, true );
							break;
						}
						case Verification.SMS_VERIFY_TYPE: {
							setSmsVerified( u, true );
							break;
						}
					}
				} );
			}
		} else {
			messages.add( "Invalid verification" );
		}

		return messages;
	}

	void setEmailVerified( UserAccount account, boolean verified ) {

	}

	void setSmsVerified( UserAccount account, boolean verified ) {

	}

	@Async
	void sendEmailAddressVerificationMessage( UserAccount account, UserCredential credentials, Verification verification ) {
		String subject = EMAIL_SUBJECT;
		String verificationMessage = generateEmailAddressVerificationMessage( subject, verification.id(), verification.code() );
		if( verificationMessage == null ) return;

		EmailMessage message = new EmailMessage();
		message.recipient( account.email(), credentials.username() );
		message.subject( subject );
		message.message( verificationMessage );
		message.isHtml( true );
		humanInterface.email( message );
	}

	private String generateEmailAddressVerificationMessage( String subject, UUID id, String code ) {
		String link = VERIFY_ENDPOINT + "?id=" + id + "&code=" + code;

		Map<String, Object> values = new HashMap<>();
		values.put( "subject", subject );
		values.put( "link", link );
		values.put( "code", code );

		PebbleEngine engine = new PebbleEngine.Builder().build();
		PebbleTemplate compiledTemplate = engine.getTemplate( TEMPLATES_VERIFY_EMAIL_HTML );

		StringWriter writer = new StringWriter();
		try {
			compiledTemplate.evaluate( writer, values );
		} catch( IOException exception ) {
			log.error( "Unable to process email template", exception );
		}
		return writer.toString();
	}

}
