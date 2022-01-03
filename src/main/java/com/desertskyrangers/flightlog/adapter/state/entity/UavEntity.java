package com.desertskyrangers.flightlog.adapter.state.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "uav" )
public class UavEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( name = "ownerid", nullable = false, columnDefinition = "BINARY(16)" )
	private UUID ownerId;

	@Column( name = "ownertype" )
	private String ownerType;

}
