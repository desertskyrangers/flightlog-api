package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.UserAccount;
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

	public static UserAccountEntity from( UserAccount account ) {
		UserAccountEntity entity = new UserAccountEntity();
		entity.setId( account.id() );
		entity.setUsername( account.username() );
		entity.setPassword( account.password() );
		entity.setEmail( account.email() );
		return entity;
	}

}
