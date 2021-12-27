package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

// FIXME User account should really be a set of credentials and email moved to the profile

@Data
@Accessors( fluent = true )
public class UserAccount {

	private UUID id;

	private String username;

	private String password;

	private String email;

	public UserAccount() {
		id(UUID.randomUUID());
	}

}
