package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

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

	private Set<UserCredentials> credentials;

	public UserAccount() {
		id( UUID.randomUUID() );
	}

}
