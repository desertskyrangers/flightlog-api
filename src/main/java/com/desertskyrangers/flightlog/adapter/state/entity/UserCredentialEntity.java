package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.UserCredentials;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "usercredential" )
public class UserCredentialEntity {

	@Id
	private UUID id;

	private String username;

	private String password;

	public static UserCredentialEntity from( UserCredentials account ) {
		UserCredentialEntity entity = new UserCredentialEntity();
		entity.setId( account.id() );
		entity.setUsername( account.username() );
		entity.setPassword( account.password() );
		return entity;
	}

}
