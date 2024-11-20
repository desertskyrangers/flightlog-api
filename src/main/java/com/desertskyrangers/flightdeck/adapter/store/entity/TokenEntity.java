package com.desertskyrangers.flightdeck.adapter.store.entity;

import com.desertskyrangers.flightdeck.core.model.UserToken;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table( name = "token" )
@Accessors( chain = true )
public class TokenEntity {

	@Id
	private UUID id;

	@ManyToOne( optional = false, fetch = FetchType.EAGER )
	@JoinColumn( name = "userid", nullable = false, updatable = false )
	private UserEntity user;

	@Column( unique = true )
	private String principal;

	private String credential;

	public static TokenEntity from( UserToken token ) {
		TokenEntity entity = fromTokenShallow( token );

		Map<UUID, UserEntity> users = new HashMap<>();
		Map<UUID, TokenEntity> tokens = new HashMap<>();
		entity.setUser( UserEntity.fromUserFromToken( token.user(), users, tokens ) );

		return entity;
	}

	static TokenEntity fromTokenFromUser( UserToken token, Map<UUID, TokenEntity> tokens, Map<UUID, UserEntity> users ) {
		TokenEntity entity = tokens.get( token.id() );
		if( entity != null ) return entity;

		entity = fromTokenShallow( token );
		tokens.put( token.id(), entity );
		entity.setUser( UserEntity.fromUserFromToken( token.user(), users, tokens ) );
		return entity;
	}

	private static TokenEntity fromTokenShallow( UserToken token ) {
		TokenEntity entity = new TokenEntity();
		entity.setId( token.id() );
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
