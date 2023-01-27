package com.desertskyrangers.flightdeck.core.model;

import java.util.UUID;

public class Entity {

	private Type type;

	private UUID id;

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
