package com.desertskyrangers.flightdeck.adapter.store.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 * @deprecated Part of an old projection strategy see {@link ProjectionEntity}
 */
@Data
@Entity
@Table( name = "preferences" )
@Accessors( chain = true )
@Deprecated
public class PreferencesProjection {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( columnDefinition = "TEXT" )
	private String json;

}
