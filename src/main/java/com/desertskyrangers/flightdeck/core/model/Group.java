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

	private GroupType type;

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

}
