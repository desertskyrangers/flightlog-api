package com.desertskyrangers.flightdeck.adapter.store.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "award" )
@Accessors( chain = true )
@SuppressWarnings( { "JpaDataSourceORMInspection", "SpellCheckingInspection" } )
public class AwardEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( name = "recipienttype" )
	private String recipientType;

	@Column( name = "recipientid" )
	private UUID recipientId;

	@Column( name = "presentertype" )
	private String presenterType;

	@Column( name = "presenterid" )
	private UUID presenterId;

	@Column( name = "earneddate")
	private Long earnedDate;

	private String description;

	private String data;

}
