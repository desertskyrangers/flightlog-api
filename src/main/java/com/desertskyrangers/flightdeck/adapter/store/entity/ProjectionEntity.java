package com.desertskyrangers.flightdeck.adapter.store.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 * The new projection strategy where all projections are stored in one table.
 */
@Data
@Entity
@Table( name = "projection" )
@Accessors( chain = true )
public class ProjectionEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( columnDefinition = "TEXT" )
	private String json;

}
