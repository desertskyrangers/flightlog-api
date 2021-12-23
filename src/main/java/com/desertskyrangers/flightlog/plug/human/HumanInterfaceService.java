package com.desertskyrangers.flightlog.plug.human;

import com.desertskyrangers.flightlog.core.model.EmailMessage;
import com.desertskyrangers.flightlog.core.model.SmsMessage;
import com.desertskyrangers.flightlog.port.HumanInterface;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class HumanInterfaceService implements HumanInterface {

	private static final String REPLY_TO_ADDRESS = "noreply@desertskyrangers.com";

	private final JavaMailSender emailSender;

	public HumanInterfaceService( JavaMailSender emailSender ) {
		this.emailSender = emailSender;
	}

	public void email( EmailMessage message ) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom( REPLY_TO_ADDRESS );
		mailMessage.setTo( message.recipients().toArray( new String[]{} ) );
		mailMessage.setSubject( message.subject() );
		mailMessage.setText( message.message() );
		emailSender.send( mailMessage );
	}

	public void sms( SmsMessage message ) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom( REPLY_TO_ADDRESS );
//		mailMessage.setTo( number + provider );
//		mailMessage.setSubject( message.subject() );
//		mailMessage.setText( message.message() );
		emailSender.send( mailMessage );
	}

}
