package com.desertskyrangers.flightdeck.adapter.store.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "award" )
@Accessors( chain = true )
public class AwardEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( name = "recipienttype" )
	private String recipientType;

	private UUID recipient;

	// DATE

	private String data;

}
