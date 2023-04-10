package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.UUID;

@Data
@Accessors( chain = true )
public class Actor {

	public static final Actor NONE = new Actor().setId( UUID.fromString( "a8d05cd4-1db0-4c42-ba0c-2ebbfa4811e3" ) ).setType( Type.ORG );

	private UUID id;

	private Type type;

	public Actor() {}

	public Actor( User user ) {
		setType( Type.USER );
		setId( user.id() );
	}

	public Actor( Group group ) {
		setType( Type.ORG );
		setId( group.id() );
	}

	public boolean isNone() {
		return Objects.equals( id, NONE.id );
	}

	public enum Type {

		USER,
		ORG;

		public static boolean isValid( String string ) {
			try {
				valueOf( string.toUpperCase() );
				return true;
			} catch( NullPointerException | IllegalArgumentException exception ) {
				return false;
			}
		}

		public static boolean isNotValid( String string ) {
			return !isValid( string );
		}

	}

}
