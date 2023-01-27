package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Group implements Comparable<Group> {

	private UUID id = UUID.randomUUID();

	private Type type;

	private String name;

	private UUID dashboardId;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Member> members = Set.of();

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<User> users = Set.of();

	@Override
	public int compareTo( Group that ) {
		return this.name.compareTo( that.name );
	}

	public enum Type {

		CLUB( "Club", "member" ),
		COMPANY( "Company", "employee" ),
		GROUP( "Group", "member" );

		private final String name;

		private final String memberTitle;

		Type( String name, String memberTitle ) {
			this.name = name;
			this.memberTitle = memberTitle;
		}

		public String getName() {
			return name;
		}

		public String getMemberTitle() {
			return memberTitle;
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
