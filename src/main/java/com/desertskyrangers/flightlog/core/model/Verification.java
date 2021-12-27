package com.desertskyrangers.flightlog.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Verification {

	private UUID id;

	private UUID userId;

	private Long timestamp;

	private String code;

	private String type;

	public Verification() {
		id(UUID.randomUUID());
	}

}
