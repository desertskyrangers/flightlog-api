package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class UserProfile {

	private final UserAccount user;

	private String preferredName;

	private String smsNumber;

	private SmsProvider provider;

	public UUID getId() {
		return user.id();
	}

}
