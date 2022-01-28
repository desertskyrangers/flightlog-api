package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.UserToken;
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
	private UserEntity user;

	@Column( unique = true )
	private String principal;

	private String credential;

	public static TokenEntity from( UserToken token ) {
		TokenEntity entity = new TokenEntity();
		entity.setId( token.id() );
		if( token.user() != null ) entity.setUser( UserEntity.fromWithoutCredential( token.user() ) );
		entity.setPrincipal( token.principal() );
		entity.setCredential( token.credential() );
		return entity;
	}

	public static UserToken toUserToken( TokenEntity entity ) {
		return toUserToken( entity, false );
	}

	public static UserToken toUserTokenDeep( TokenEntity entity ) {
		return toUserToken( entity, true );
	}

	private static UserToken toUserToken( TokenEntity entity, boolean includeUser ) {
		UserToken credential = new UserToken();

		credential.id( entity.getId() );
		credential.principal( entity.getPrincipal() );
		credential.credential( entity.getCredential() );
		if( includeUser && entity.getUser() != null ) credential.user( UserEntity.toUser( entity.getUser() ) );

		return credential;
	}

}
