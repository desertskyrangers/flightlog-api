package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.core.model.EmailMessage;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.core.model.UserToken;
import com.desertskyrangers.flightdeck.core.model.Verification;
import com.desertskyrangers.flightdeck.port.*;
import com.desertskyrangers.flightdeck.util.Email;
import com.desertskyrangers.flightdeck.util.Text;
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
public class AuthServiceImpl implements AuthService {

	private static final String RESET_ENDPOINT = ApiPath.HOST + "/reset";

	private static final String VERIFY_ENDPOINT = ApiPath.HOST + "/verify";

	private static final String RECOVERY_EMAIL_SUBJECT = "FlightDeck Account Recovery";

	private static final String RECOVERY_EMAIL_TEMPLATE = "templates/account-recovery.html";

	private static final String VERIFY_EMAIL_SUBJECT = "FlightDeck Email Account Verification";

	private static final String VERIFY_EMAIL_TEMPLATE = "templates/verify-email.html";

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private final HumanInterface humanInterface;

	private final PasswordEncoder passwordEncoder;

	private final UserService userService;

	public AuthServiceImpl( StatePersisting statePersisting, StateRetrieving stateRetrieving, HumanInterface humanInterface, PasswordEncoder passwordEncoder, UserService userService ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
		this.humanInterface = humanInterface;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}

	@Scheduled( fixedRate = 1, timeUnit = TimeUnit.MINUTES )
	void cleanupExpiredVerificationsAndAccounts() {
		for( Verification verification : stateRetrieving.findAllVerifications() ) {
			if( verification.isExpired() ) {
				statePersisting.remove( verification );
				//stateRetrieving.findUserAccount( verification.userId() ).ifPresent( statePersisting::remove );
				log.info( "Verification expired: " + verification.id() );
			}
		}
	}

	@Override
	public List<String> requestUserRecover( String username ) {
		log.info( "Account recover username=" + username );
		List<String> messages = new ArrayList<>();

		Optional<UserToken> optional = stateRetrieving.findUserTokenByPrincipal( username );
		if( optional.isPresent() ) {
			UserToken token = optional.get();
			User user = token.user();
			Verification verification = statePersisting.upsert( new Verification().userId( user.id() ) );
			sendAccountRecoveryMessage( user, verification.id().toString() );
			log.warn( "recovery code: " + verification.id() );
			messages.add( "Recovery email sent" );
		} else {
			messages.add( "Username or email not found" );
		}

		return messages;
	}

	@Override
	public List<String> requestUserReset( Verification verification, String password ) {
		log.info( "Requesting user reset..." );
		List<String> messages = new ArrayList<>();

		Optional<Verification> optional = stateRetrieving.findVerification( verification.id() );
		if( optional.isPresent() ) {
			Verification storedVerification = optional.get();

			boolean isValid = storedVerification.isValid( verification.timestamp() );
			boolean isExpired = storedVerification.isExpired( verification.timestamp() );

			if( !isValid ) messages.add( "Invalid recovery timestamp" );
			if( isExpired ) messages.add( "Recovery code expired" );

			if( messages.size() == 0 ) {
				stateRetrieving.findUserAccount( storedVerification.userId() ).ifPresent( u -> userService.updatePassword( u, password ) );
				statePersisting.remove( storedVerification );
			}
		} else {
			messages.add( "Recovery code expired" );
		}

		return messages;
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
		if( !validEmail ) messages.add( "Invalid email address" );

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
		User account = new User();
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

	void setEmailVerified( User account, boolean verified ) {
		account.emailVerified( verified );
		statePersisting.upsert( account );
		log.info( "Email verified=" + verified + " address=" + account.email() );
	}

	void setSmsVerified( User account, boolean verified ) {
		account.smsVerified( verified );
		statePersisting.upsert( account );
		log.info( "SMS verified=" + verified + " number=" + account.smsNumber() );
	}

	@Async
	void sendAccountRecoveryMessage( User account, String id ) {
		String subject = RECOVERY_EMAIL_SUBJECT;
		String link = generateRecoveryLink( id );
		String message = createFromTemplate( RECOVERY_EMAIL_TEMPLATE, Map.of( "subject", subject, "link", link ) );
		if( message == null ) return;

		EmailMessage email = new EmailMessage();
		email.recipient( account.email(), account.preferredName() );
		email.subject( subject );
		email.message( message );
		email.isHtml( true );
		humanInterface.email( email );
	}

	// This link is intentionally not a link to /api/auth/recover
	// it is supposed to request the reset page at the browser.
	String generateRecoveryLink( String id ) {
		return RESET_ENDPOINT + "?id=" + id;
	}

	@Async
	void sendEmailAddressVerificationMessage( User account, String name, Verification verification ) {
		String subject = VERIFY_EMAIL_SUBJECT;
		String message = generateEmailAddressVerificationMessage( subject, verification.id(), verification.code() );
		if( message == null ) return;

		EmailMessage email = new EmailMessage();
		email.recipient( account.email(), name );
		email.subject( subject );
		email.message( message );
		email.isHtml( true );
		humanInterface.email( email );
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
		return createFromTemplate( VERIFY_EMAIL_TEMPLATE, values );
	}

	private String createFromTemplate( String template, Map<String, Object> values ) {
		PebbleEngine engine = new PebbleEngine.Builder().build();
		PebbleTemplate compiledTemplate = engine.getTemplate( template );

		StringWriter writer = new StringWriter();
		try {
			compiledTemplate.evaluate( writer, values );
		} catch( IOException exception ) {
			log.error( "Unable to process email template", exception );
			return null;
		}
		return writer.toString();
	}

}
