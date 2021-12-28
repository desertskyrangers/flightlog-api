package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.UserCredential;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table( name = "usercredential" )
public class UserCredentialEntity {

	@Id
	private UUID id;

	@ManyToOne( optional = false, fetch = FetchType.LAZY )
	@JoinColumn( name = "userid", nullable = false, updatable = false )
	private UserAccountEntity userAccount;

	private String username;

	private String password;

	public static UserCredentialEntity from( UserCredential credentials ) {
		UserCredentialEntity entity = new UserCredentialEntity();
		entity.setId( credentials.id() );
		//entity.setUserAccount( UserAccountEntity.from(credentials.userAccount()) );
		entity.setUsername( credentials.username() );
		entity.setPassword( credentials.password() );
		return entity;
	}

}
