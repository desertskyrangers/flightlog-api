package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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

	@Getter
	public enum Status {

		OWNER( "Owner", true ),
		ACCEPTED( "Accepted", true ),
		INVITED( "Invited", false ),
		REQUESTED( "Requested", false ),
		REVOKED( "Revoked", false );

		private final String title;

		private final boolean active;

		Status( String title, boolean active ) {
			this.title = title;
			this.active = active;
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
