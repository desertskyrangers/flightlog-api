package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Member {

	@EqualsAndHashCode.Exclude
	private UUID id = UUID.randomUUID();

	private Group group;

	private User user;

	@EqualsAndHashCode.Exclude
	private MemberStatus status;

}
