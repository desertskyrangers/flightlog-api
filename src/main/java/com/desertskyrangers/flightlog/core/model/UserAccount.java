package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class UserAccount {

	private  UUID id;

	private  String username;

	private  String password;

	private  String email;

}
