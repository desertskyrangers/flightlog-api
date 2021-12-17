package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Accessors( fluent = true )
@Entity
@Table( name = "useraccount" )
public class UserAccount {

	@Id
	private UUID id;

	private String username;

	private String password;

	private String email;

}
