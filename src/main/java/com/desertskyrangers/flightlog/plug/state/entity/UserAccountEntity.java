package com.desertskyrangers.flightlog.plug.state.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "useraccount" )
public class UserAccountEntity {

	@Id
	private UUID id;

	private String username;

	private String password;

	private String email;

}
