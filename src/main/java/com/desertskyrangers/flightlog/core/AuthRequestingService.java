package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.adapter.api.ApiPath;
import com.desertskyrangers.flightlog.core.model.EmailMessage;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserToken;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.port.HumanInterface;
import com.desertskyrangers.flightlog.port.StatePersisting;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import com.desertskyrangers.flightlog.util.Email;
import com.desertskyrangers.flightlog.util.Text;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthRequestingService implements AuthRequesting {

	private static final String VERIFY_ENDPOINT = ApiPath.HOST + "/verify";

	private static final String EMAIL_SUBJECT = "FlightLog Email Account Verification";

	private static final String TEMPLATES_VERIFY_EMAIL_HTML = "templates/verify-email.html";

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private final HumanInterface humanInterface;

	private final PasswordEncoder passwordEncoder;

	public AuthRequestingService( StatePersisting statePersisting, StateRetrieving stateRetrieving, HumanInterface humanInterface, PasswordEncoder passwordEncoder ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
		this.humanInterface = humanInterface;
		this.passwordEncoder = passwordEncoder;
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
	public List<String> requestUserRegister( String username, String email, String password, UUID verifyId ) {
		log.info( "Account register username=" + username + " email=" + email );

		List<String> messages = new ArrayList<>();

		// Check for invalid data
		boolean usernameTooShort = username.length() < 1;
		boolean usernameTooLong = username.length() > 63;
		boolean passwordTooShort = password.length() < 8;
		boolean passwordTooLong = password.length() > 127;
		boolean validUsername = !usernameTooShort && !usernameTooLong;
		boolean validPassword = !passwordTooShort && !passwordTooLong;
		boolean validEmail = Email.isValid( email );

		if( !validUsername ) messages.add( "Invalid username" );
		if( !validPassword ) messages.add( "Invalid password" );
		if( !validEmail ) messages.add( "Invalid password" );

		// Check and create tokens
		Set<UserToken> tokens = new HashSet<>();
		String encodedPassword = passwordEncoder.encode( password );
		if( validUsername && validPassword ) {
			stateRetrieving.findUserTokenByPrincipal( username ).ifPresentOrElse( t -> {
				log.warn( "Username not available: username=" + username );
				messages.add( "Username not available" );
			}, () -> tokens.add( new UserToken().principal( username ).credential( encodedPassword ) ) );
		}
		if( validEmail && validPassword ) {
			stateRetrieving.findUserTokenByPrincipal( email ).ifPresentOrElse( t -> {
				log.warn( "Email not available: email=" + email );
				messages.add( "Email not available" );
			}, () -> tokens.add( new UserToken().principal( email ).credential( encodedPassword ) ) );
		}
		if( !messages.isEmpty() ) return messages;

		// Create the account
		UserAccount account = new UserAccount();
		account.email( email );
		account.tokens( tokens );
		statePersisting.upsert( account );

		// Generate the verification code
		String code = Text.lpad( String.valueOf( new Random().nextInt( 1000000 ) ), 6, '0' );

		// Store the verification record
		Verification verification = new Verification().id( verifyId );
		verification.userId( account.id() );
		verification.code( code );
		verification.type( Verification.EMAIL_VERIFY_TYPE );
		statePersisting.upsert( verification );

		log.warn( "verification code: " + verification.code() );

		// Send the message to verify the email address
		sendEmailAddressVerificationMessage( account, username, verification );

		return List.of();
	}

	@Override
	public List<String> requestUserVerifyResend( UUID id ) {
		List<String> messages = new ArrayList<>();

		// Lookup the verification from the state store
		stateRetrieving.findVerification( id ).ifPresent( v -> {
			stateRetrieving.findUserAccount( v.userId() ).ifPresent( u -> {
				UserToken credential = u.tokens().iterator().next();

				log.warn( "verification code resent: " + v.code() );

				// Send the message to verify the email address
				sendEmailAddressVerificationMessage( u, credential.principal(), v );
				messages.add( "Verification email resent" );
			} );
		} );

		return messages;
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
	void sendEmailAddressVerificationMessage( UserAccount account, String name, Verification verification ) {
		String subject = EMAIL_SUBJECT;
		String verificationMessage = generateEmailAddressVerificationMessage( subject, verification.id(), verification.code() );
		if( verificationMessage == null ) return;

		EmailMessage message = new EmailMessage();
		message.recipient( account.email(), name );
		message.subject( subject );
		message.message( verificationMessage );
		message.isHtml( true );
		humanInterface.email( message );
	}

	// This link is intentionally not a link to /api/auth/verify
	// it is supposed to request the verify page at the browser.
	String generateVerifyLink( UUID id, String code ) {
		return VERIFY_ENDPOINT + "/" + id + "/" + code;
	}

	private String generateEmailAddressVerificationMessage( String subject, UUID id, String code ) {
		Map<String, Object> values = new HashMap<>();
		values.put( "subject", subject );
		values.put( "link", generateVerifyLink( id, code ) );
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
