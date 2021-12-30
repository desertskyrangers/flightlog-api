package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table( name = "credential" )
public class CredentialEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@ManyToOne( optional = false, fetch = FetchType.LAZY )
	@JoinColumn( name = "userid", nullable = false, updatable = false, columnDefinition = "BINARY(16)" )
	private UserEntity userAccount;

	private String username;

	private String password;

	public static CredentialEntity from( UserCredential credentials ) {
		CredentialEntity entity = new CredentialEntity();
		entity.setId( credentials.id() );
		if( credentials.userAccount() != null ) entity.setUserAccount( UserEntity.fromWithoutCredential( credentials.userAccount() ) );
		entity.setUsername( credentials.username() );
		entity.setPassword( credentials.password() );
		return entity;
	}

	public static UserCredential toUserCredential( CredentialEntity entity ) {
		UserCredential credential = new UserCredential();

		credential.id( entity.getId() );
		credential.username( entity.getUsername() );
		credential.password( entity.getPassword() );
		if( entity.getUserAccount() != null ) credential.userAccount( UserEntity.toUserAccount( entity.getUserAccount() ));

		return credential;
	}

	public static UserCredential toUserCredential( UserAccount account, CredentialEntity entity ) {
		UserCredential credential = toUserCredential( entity );

		credential.userAccount( account );

		return credential;
	}

}
