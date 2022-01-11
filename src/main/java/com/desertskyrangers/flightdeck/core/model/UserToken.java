package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class UserToken {

	private UUID id = UUID.randomUUID();

	private String principal;

	private String credential;

	@EqualsAndHashCode.Exclude
	private User user;

}
