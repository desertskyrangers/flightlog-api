package com.desertskyrangers.flightdeck.adapter.state.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table( name = "org" )
public class GroupEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column(nullable = false)
	String type;

	@Column(nullable = false)
	String name;

	// This is a user account id
	@ManyToOne( optional = false, fetch = FetchType.EAGER )
	@JoinColumn( name="ownerid", nullable = false, updatable = false, columnDefinition = "BINARY(16)" )
	private UserEntity owner;

}
