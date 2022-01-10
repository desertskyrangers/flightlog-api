package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
@Accessors( fluent = true )
public class User {

	private UUID id = UUID.randomUUID();

	private String firstName;

	private String lastName;

	private String preferredName;

	private String email;

	private boolean emailVerified;

	private String smsNumber;

	private SmsCarrier smsCarrier;

	private boolean smsVerified;

	@EqualsAndHashCode.Exclude
	private Set<UserToken> tokens = new CopyOnWriteArraySet<>();

	@EqualsAndHashCode.Exclude
	private Set<String> roles = new CopyOnWriteArraySet<>();

	public void roles( Set<String> roles ) {
		this.roles = roles == null ? Set.of() : roles;
	}

}
