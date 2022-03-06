package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.*;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "battery" )
@Accessors( chain = true )
public class BatteryEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	private String name;

	private String make;

	private String model;

	private String connector;

	@Column( name = "unlistedconnector" )
	private String unlistedConnector;

	private String status;

	private String type;

	private int cells;

	@Column( name = "initialcycles" )
	private int initialCycles;

	private int cycles;

	private int capacity;

	@Column( name = "dischargerating" )
	private int dischargeRating;

	@Column( nullable = false, columnDefinition = "BINARY(16)" )
	private UUID owner;

	@Column( name = "ownertype", nullable = false )
	private String ownerType;

	public static BatteryEntity from( Battery battery ) {
		BatteryEntity entity = new BatteryEntity();

		entity.setId( battery.id() );
		entity.setName( battery.name() );
		if( battery.status() != null ) entity.setStatus( battery.status().name().toLowerCase() );
		entity.setMake( battery.make() );
		entity.setModel( battery.model() );
		if( battery.connector() != null ) entity.setConnector( battery.connector().name().toLowerCase() );
		entity.setUnlistedConnector( battery.unlistedConnector() );
		if( battery.type() != null ) entity.setType( battery.type().name().toLowerCase() );
		entity.setCells( battery.cells() );
		entity.setInitialCycles( battery.initialCycles() );
		entity.setCycles( battery.cycles() );
		entity.setCapacity( battery.capacity() );
		entity.setDischargeRating( battery.dischargeRating() );
		entity.setOwner( battery.owner() );
		if( battery.ownerType() != null ) entity.setOwnerType( battery.ownerType().name().toLowerCase() );

		return entity;
	}

	public static Battery toBattery( BatteryEntity entity ) {
		Battery battery = new Battery();

		battery.id( entity.getId() );
		battery.name( entity.getName() );
		if( entity.getStatus() != null ) battery.status( BatteryStatus.valueOf( entity.getStatus().toUpperCase() ) );
		battery.make( entity.getMake() );
		battery.model( entity.getModel() );
		if( entity.getConnector() != null ) battery.connector( BatteryConnector.valueOf( entity.getConnector().toUpperCase() ) );
		battery.unlistedConnector( entity.getUnlistedConnector() );
		if( entity.getType() != null ) battery.type( BatteryType.valueOf( entity.getType().toUpperCase() ) );
		battery.cells( entity.getCells() );
		battery.initialCycles( entity.getInitialCycles() );
		battery.cycles( entity.getCycles() );
		battery.capacity( entity.getCapacity() );
		battery.dischargeRating( entity.getDischargeRating() );
		battery.owner( entity.getOwner() );
		if( entity.getOwnerType() != null ) battery.ownerType( OwnerType.valueOf( entity.getOwnerType().toUpperCase() ) );

		return battery;
	}

}
