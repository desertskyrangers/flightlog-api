package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Group {

	UUID id = UUID.randomUUID();

	GroupType type;

	String name;

	User owner;

	Set<User> members = Set.of();

}
