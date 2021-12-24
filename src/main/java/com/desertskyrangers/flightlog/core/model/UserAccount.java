package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.util.UUID;

@Data
@Accessors( fluent = true )
public class UserAccount {

	@Id
	private UUID id;

	private String username;

	private String password;

	private String email;

}
