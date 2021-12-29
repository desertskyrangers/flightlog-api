package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Verification {

	public static final String EMAIL_VERIFY_TYPE = "email";

	public static final String SMS_VERIFY_TYPE = "sms";

	public static final long CODE_TIMEOUT = 600000;

	private UUID id;

	private UUID userId;

	private Long timestamp;

	private String code;

	private String type;

	public Verification() {
		id( UUID.randomUUID() );
		timestamp( System.currentTimeMillis() );
	}

}
