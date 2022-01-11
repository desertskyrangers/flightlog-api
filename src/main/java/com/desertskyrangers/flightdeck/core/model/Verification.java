package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Verification {

	public static final String EMAIL_VERIFY_TYPE = "email";

	public static final String SMS_VERIFY_TYPE = "sms";

	public static final long CODE_TIMEOUT = 600000;

	private UUID id = UUID.randomUUID();

	private UUID userId;

	private Long timestamp = System.currentTimeMillis();

	private String code;

	private String type;

	public boolean isValid() {
		return isValid( System.currentTimeMillis() );
	}

	public boolean isValid( long timestamp ) {
		return timestamp - timestamp() >= 0;
	}

	public boolean isExpired() {
		return isExpired( System.currentTimeMillis() );
	}

	public boolean isExpired( long timestamp ) {
		return timestamp - timestamp() > CODE_TIMEOUT;
	}

}
