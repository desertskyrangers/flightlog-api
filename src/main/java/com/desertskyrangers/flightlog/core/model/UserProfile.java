package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

// FIXME The profile should be the user account, email moved to this class and account "just" be credentials

@Data
@Accessors( fluent = true )
public class UserProfile {

	private final UserAccount user;

	private String preferredName;

	private String smsNumber;

	private SmsProvider smsProvider;

	public UUID getId() {
		return user.id();
	}

}
