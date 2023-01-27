package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Member implements Comparable<Member> {

	private UUID id = UUID.randomUUID();

	private Group group;

	private User user;

	@EqualsAndHashCode.Exclude
	private Status status;

	@Override
	public int compareTo( Member that ) {
		return this.status.compareTo( that.status );
	}

	public enum Status {

		OWNER( "Owner" ),
		ACCEPTED( "Accepted" ),
		INVITED( "Invited" ),
		REQUESTED( "Requested" ),
		REVOKED( "Revoked" );

		private final String name;

		Status( String name ) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

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
