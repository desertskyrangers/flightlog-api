package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.EmailMessage;
import com.desertskyrangers.flightdeck.core.model.SmsMessage;
import org.springframework.scheduling.annotation.Async;

public interface HumanInterface {

	@Async
	void email( EmailMessage message );

	@Async
	void sms( SmsMessage message );

}
