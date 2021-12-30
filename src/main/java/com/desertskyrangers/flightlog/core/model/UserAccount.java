package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
@Accessors( fluent = true )
public class UserAccount {

	private UUID id;

	private String firstName;

	private String lastName;

	private String preferredName;

	private String email;

	private boolean emailVerified;

	private String smsNumber;

	private SmsProvider smsProvider;

	private boolean smsVerified;

	private Set<UserCredential> credentials = new CopyOnWriteArraySet<>();

	private Set<String> roles = new CopyOnWriteArraySet<>();

	public UserAccount() {
		id( UUID.randomUUID() );
	}

}
