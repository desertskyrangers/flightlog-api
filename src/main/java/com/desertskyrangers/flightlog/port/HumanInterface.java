package com.desertskyrangers.flightlog.port;

import com.desertskyrangers.flightlog.core.model.EmailMessage;
import com.desertskyrangers.flightlog.core.model.SmsMessage;

public interface HumanInterface {

	void email( EmailMessage message );

	void sms( SmsMessage message );
}
