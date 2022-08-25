package com.desertskyrangers.flightdeck.adapter.human;

import com.desertskyrangers.flightdeck.FlightDeckApp;
import com.desertskyrangers.flightdeck.core.model.EmailMessage;
import com.desertskyrangers.flightdeck.core.model.SmsMessage;
import com.desertskyrangers.flightdeck.port.HumanInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
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
		EmailMessage mailMessage = new EmailMessage();
		mailMessage.recipients( message.recipients() );
		mailMessage.subject( message.message() );
		mailMessage.message( "" );
		email( mailMessage );
	}

}
