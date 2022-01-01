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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

	@Scheduled( fixedRate = 1, timeUnit = TimeUnit.MINUTES )
	public void cleanupExpiredVerificationsAndAccounts() {
		for( Verification verification : stateRetrieving.findAllVerifications() ) {
			if( verification.isExpired() ) {
				stateRetrieving.findUserAccount( verification.userId() ).ifPresent( statePersisting::remove );
				statePersisting.remove( verification );
				log.info( "Verification expired: " + verification.id() );
			}
		}
	}

	@Override
	public List<String> requestUserRegister( UserAccount account, UserCredential credentials, Verification verification ) {
		log.info( "Creating account username=" + credentials.username() + " email=" + account.email() );

		// Block repeat attempts to generate the same account
		Optional<UserCredential> optional = stateRetrieving.findUserCredentialByUsername( credentials.username() );
		if( optional.isPresent() ) return List.of( "Username not available" );

		// Store the new account
		account.credentials().add( credentials );
		statePersisting.upsert( account );

		// Generate the verification code
		String code = Text.lpad( String.valueOf( new Random().nextInt( 1000000 ) ), 6, '0' );

		// Store the verification record
		statePersisting.upsert( verification.userId( account.id() ).code( code ).type( Verification.EMAIL_VERIFY_TYPE ) );

		log.warn( "verification code: " + verification.code() );

		// Send the message to verify the email address
		sendEmailAddressVerificationMessage( account, credentials, verification );

		return List.of();
	}

	@Override
	public List<String> requestUserVerify( Verification verification ) {
		List<String> messages = new ArrayList<>();

		Optional<Verification> optional = stateRetrieving.findVerification( verification.id() );
		if( optional.isPresent() ) {
			Verification storedVerification = optional.get();

			boolean validCode = Objects.equals( storedVerification.code(), verification.code() );
			boolean isValid = storedVerification.isValid( verification.timestamp() );
			boolean isExpired = storedVerification.isExpired( verification.timestamp() );

			if( !validCode ) messages.add( "Invalid verification code: " + verification.code() );
			if( !isValid ) messages.add( "Invalid verification timestamp" );
			if( isExpired ) messages.add( "Verification code expired" );

			if( messages.size() == 0 ) {
				stateRetrieving.findUserAccount( storedVerification.userId() ).ifPresent( u -> {
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
				statePersisting.remove( storedVerification );
			}
		} else {
			messages.add( "Verification code expired" );
		}

		return messages;
	}

	@Deprecated
	@Override
	public UserCredential getUserCredential( UUID userId ) {
		UserAccount account = stateRetrieving.findUserAccount( userId ).orElseThrow();
		return account.credentials().iterator().next();
	}

	void setEmailVerified( UserAccount account, boolean verified ) {
		account.emailVerified( verified );
		statePersisting.upsert( account );
		log.info( "Email verified=" + verified + " address=" + account.email() );
	}

	void setSmsVerified( UserAccount account, boolean verified ) {
		account.smsVerified( verified );
		statePersisting.upsert( account );
		log.info( "SMS verified=" + verified + " number=" + account.smsNumber() );
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
		//String link = VERIFY_ENDPOINT + "?id=" + id + "&code=" + code;
		String link = "/verify/" + id + "/" + code;

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
