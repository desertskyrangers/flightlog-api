package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class UserCredential {

	private UUID id;

	private UserAccount userAccount;

	private String username;

	private String password;

	public UserCredential() {
		id(UUID.randomUUID());
	}

}
