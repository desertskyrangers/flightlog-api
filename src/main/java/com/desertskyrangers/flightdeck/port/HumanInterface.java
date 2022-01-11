package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.EmailMessage;
import com.desertskyrangers.flightdeck.core.model.SmsMessage;

public interface HumanInterface {

	void email( EmailMessage message );

	void sms( SmsMessage message );
}
