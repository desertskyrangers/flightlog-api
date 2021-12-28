package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

// FIXME The profile should be the user account, email moved to this class and account "just" be credentials

@Data
@Accessors( fluent = true )
public class UserAccount {

	private UUID id;

	private String username;

	// given name

	// last name

	private String preferredName;

	private String email;

	private boolean emailVerified;

	private String smsNumber;

	private SmsProvider smsProvider;

	private boolean smsVerified;

	// authentications

	public UserAccount() {
		id( UUID.randomUUID() );
	}

}
