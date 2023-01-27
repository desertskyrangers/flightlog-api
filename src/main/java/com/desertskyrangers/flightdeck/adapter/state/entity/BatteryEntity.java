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

	@Column( name = "type" )
	private String chemistry;

	private int cells;

	@Column( name = "initialcycles" )
	private int initialCycles;

	private int cycles;

	private int capacity;

	@Column( name = "dischargerating" )
	private int dischargeRating;

	@Column( name = "flightcount" )
	private Integer flightCount;

	@Column( name = "flighttime" )
	private Long flightTime;

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
		if( battery.chemistry() != null ) entity.setChemistry( battery.chemistry().name().toLowerCase() );
		entity.setCells( battery.cells() );
		entity.setInitialCycles( battery.initialCycles() );
		entity.setCycles( battery.cycles() );
		entity.setCapacity( battery.capacity() );
		entity.setDischargeRating( battery.dischargeRating() );

		entity.setFlightCount( battery.flightCount() );
		entity.setFlightTime( battery.flightTime() );

		entity.setOwner( battery.owner() );
		if( battery.ownerType() != null ) entity.setOwnerType( battery.ownerType().name().toLowerCase() );

		return entity;
	}

	public static Battery toBattery( BatteryEntity entity ) {
		Battery battery = new Battery();

		battery.id( entity.getId() );
		battery.name( entity.getName() );
		if( entity.getStatus() != null ) battery.status( Battery.Status.valueOf( entity.getStatus().toUpperCase() ) );
		battery.make( entity.getMake() );
		battery.model( entity.getModel() );
		if( entity.getConnector() != null ) battery.connector( Battery.Connector.valueOf( entity.getConnector().toUpperCase() ) );
		battery.unlistedConnector( entity.getUnlistedConnector() );
		if( entity.getChemistry() != null ) battery.chemistry( Battery.Chemistry.valueOf( entity.getChemistry().toUpperCase() ) );
		battery.cells( entity.getCells() );
		battery.initialCycles( entity.getInitialCycles() );
		battery.cycles( entity.getCycles() );
		battery.capacity( entity.getCapacity() );
		battery.dischargeRating( entity.getDischargeRating() );

		battery.flightCount( entity.getFlightCount() == null ? 0 : entity.getFlightCount() );
		battery.flightTime( entity.getFlightTime() == null ? 0 : entity.getFlightTime() );

		battery.owner( entity.getOwner() );
		if( entity.getOwnerType() != null ) battery.ownerType( OwnerType.valueOf( entity.getOwnerType().toUpperCase() ) );

		return battery;
	}

}
