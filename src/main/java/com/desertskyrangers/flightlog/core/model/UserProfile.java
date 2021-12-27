package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

// FIXME The profile should be the user account, email moved to this class and account "just" be credentials

@Data
@Accessors( fluent = true )
public class UserProfile {

	private UUID id;

	// given name

	// last name

	private String preferredName;

	// email

	private String smsNumber;

	private SmsProvider smsProvider;

	// authentications

	public UserProfile() {
		id( UUID.randomUUID() );
	}

}
