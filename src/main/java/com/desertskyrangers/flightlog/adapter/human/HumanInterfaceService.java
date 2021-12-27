package com.desertskyrangers.flightlog.adapter.human;

import com.desertskyrangers.flightlog.FlightLogApp;
import com.desertskyrangers.flightlog.core.model.EmailMessage;
import com.desertskyrangers.flightlog.core.model.SmsMessage;
import com.desertskyrangers.flightlog.port.HumanInterface;
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

	private static final String REPLY_TO_ADDRESS = "noreply@desertskyrangers.com";

	private static final String REPLY_TO_NAME = "Desert Sky Rangers";

	private final FlightLogApp app;

	private final JavaMailSender emailSender;

	public HumanInterfaceService( FlightLogApp app, JavaMailSender emailSender ) {
		this.app = app;
		this.emailSender = emailSender;
	}

	public void email( EmailMessage message ) {
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper( mimeMessage );
			helper.setFrom( REPLY_TO_ADDRESS, REPLY_TO_NAME );
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
		}
	}

	public void sms( SmsMessage message ) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom( REPLY_TO_ADDRESS );
		//		mailMessage.setTo( number + provider );
		//		mailMessage.setSubject( message.subject() );
		//		mailMessage.setText( message.message() );
		//		emailSender.send( mailMessage );
	}

}
