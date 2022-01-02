package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.UserToken;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table( name = "token" )
public class TokenEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@ManyToOne( optional = false, fetch = FetchType.EAGER )
	@JoinColumn( name = "userid", nullable = false, updatable = false, columnDefinition = "BINARY(16)" )
	private UserEntity userAccount;

	@Column( unique = true )
	private String principal;

	private String credential;

	public static TokenEntity from( UserToken token ) {
		TokenEntity entity = new TokenEntity();
		entity.setId( token.id() );
		if( token.userAccount() != null ) entity.setUserAccount( UserEntity.fromWithoutCredential( token.userAccount() ) );
		entity.setPrincipal( token.principal() );
		entity.setCredential( token.credential() );
		return entity;
	}

	public static UserToken toUserCredential( TokenEntity entity ) {
		return toUserCredential( entity, false );
	}

	public static UserToken toUserCredentialDeep( TokenEntity entity ) {
		return toUserCredential( entity, true );
	}

	private static UserToken toUserCredential( TokenEntity entity, boolean includeAccount ) {
		UserToken credential = new UserToken();

		credential.id( entity.getId() );
		credential.principal( entity.getPrincipal() );
		credential.credential( entity.getCredential() );
		if( includeAccount && entity.getUserAccount() != null ) credential.userAccount( UserEntity.toUserAccount( entity.getUserAccount() ) );

		return credential;
	}

}
