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

	@EqualsAndHashCode.Exclude
	UUID id = UUID.randomUUID();

	GroupType type;

	String name;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	Set<User> users = Set.of();

	@Override
	public int compareTo( Group that ) {
		return this.name.compareTo( that.name );
	}

}
