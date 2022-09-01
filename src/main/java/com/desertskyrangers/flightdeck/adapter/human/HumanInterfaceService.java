package com.desertskyrangers.flightdeck.adapter.human;

import com.desertskyrangers.flightdeck.FlightDeckApp;
import com.desertskyrangers.flightdeck.core.model.EmailMessage;
import com.desertskyrangers.flightdeck.core.model.SmsMessage;
import com.desertskyrangers.flightdeck.port.HumanInterface;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@Slf4j
public class HumanInterfaceService implements HumanInterface {

	private static final String FROM_ADDRESS = "flightdeck@desertskyrangers.org";

	private static final String FROM_NAME = "FlightDeck";

	private static final String REPLY_TO_ADDRESS = "noreply@desertskyrangers.org";

	private static final String REPLY_TO_NAME = "Desert Sky Rangers";

	private final FlightDeckApp app;

	private final JavaMailSender emailSender;

	@Value( "${flightdeck.sms.username}" )
	private String username;

	@Value( "${flightdeck.sms.password}" )
	private String password;

	@Value( "${flightdeck.sms.from: +18005550000}" )
	private String from;

	public HumanInterfaceService( FlightDeckApp app, JavaMailSender emailSender ) {
		this.app = app;
		this.emailSender = emailSender;
	}

	public void email( EmailMessage message ) {
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper( mimeMessage );
			helper.setFrom( FROM_ADDRESS, FROM_NAME );
			helper.setReplyTo( REPLY_TO_ADDRESS, REPLY_TO_NAME );
			helper.setTo( message.recipients() );
			helper.setSubject( message.subject() );
			helper.setText( message.message(), message.isHtml() );
		} catch( MessagingException | UnsupportedEncodingException exception ) {
			log.error( "Error creating email message", exception );
			return;
		}

		if( app.isProduction() ) {
			emailSender.send( mimeMessage );
		} else {
			log.debug( message.message() );
			// Simulate a delay
			try {
				Thread.sleep( 3000 );
			} catch( InterruptedException exception ) {
				// Intentionally ignore exception
			}
		}
	}

	public void sms( SmsMessage message ) {
		if( message.numbers().isEmpty() ) log.warn( "No recipients for sms: " + message.content() );

		Twilio.init( username, password );

		log.info( "Send sms " + message.content() );
		message.numbers().stream().map( HumanInterfaceService::e164Format ).forEach( n -> {
			Message twilioMessage = Message.creator( new PhoneNumber( n ), new PhoneNumber( from ), message.content() ).create();
			log.debug( "SMS message sent with sid=" + twilioMessage.getSid() );
		} );
	}

	public static String e164Format( String string ) {
		if( string == null ) return null;

		StringBuilder numbers = new StringBuilder();
		for( char c : string.toCharArray() ) {
			if( c >= '0' && c <= '9') numbers.append( c );
		}

		if( numbers.length() == 10 ) return "+1" + numbers;
		if( numbers.length() == 11 ) return "+" + numbers;

		return string;
	}

}
