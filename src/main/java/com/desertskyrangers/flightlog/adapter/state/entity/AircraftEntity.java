package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.Aircraft;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "aircraft" )
public class AircraftEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( nullable = false )
	private String name;

	private String type;

	private String make;

	private String model;

	private String status;

	@Column( nullable = false, columnDefinition = "BINARY(16)" )
	private UUID owner;

	@Column( name = "ownertype", nullable = false )
	private String ownerType;

	public static AircraftEntity from( Aircraft aircraft ) {
		AircraftEntity entity = new AircraftEntity();

		entity.setId( aircraft.id() );
		entity.setName( aircraft.name() );
		entity.setType( aircraft.type().name().toLowerCase() );
		entity.setMake( aircraft.make() );
		entity.setModel( aircraft.model() );
		entity.setStatus( aircraft.status().name().toLowerCase() );
		entity.setOwner( aircraft.ownerId() );
		entity.setOwnerType( aircraft.ownerType().name().toLowerCase() );

		return entity;
	}
}
