package com.desertskyrangers.flightdeck.adapter.api.model;

import com.desertskyrangers.flightdeck.core.model.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ReactBattery {

	private String id;

	private String name;

	private String type;

	private String make;

	private String model;

	private String status;

	private String owner;

	private String ownerType;


	public static ReactBattery from( Battery battery ) {
		ReactBattery result = new ReactBattery();

		result.setId( battery.id().toString() );
		result.setName( battery.name() );
		result.setType( battery.type().name().toLowerCase() );
		result.setMake( battery.make() );
		result.setModel( battery.model() );
		result.setStatus( battery.status().name().toLowerCase() );
		result.setOwner( battery.owner().toString() );
		result.setOwnerType( battery.ownerType().name().toLowerCase() );

		return result;
	}

	public static Battery toBattery( ReactBattery reactBattery ) {
		Battery battery = new Battery();

		battery.id( UUID.fromString( reactBattery.getId() ) );
		battery.name( reactBattery.getName() );
		battery.type( BatteryType.valueOf( reactBattery.getType().toUpperCase() ) );
		battery.make( reactBattery.getMake() );
		battery.model( reactBattery.getModel() );
		battery.status( BatteryStatus.valueOf( reactBattery.getStatus().toUpperCase() ) );
		if( reactBattery.getOwner() != null ) battery.owner( UUID.fromString( reactBattery.getOwner() ) );
		if( reactBattery.getOwnerType() != null ) battery.ownerType( OwnerType.valueOf( reactBattery.getOwnerType().toUpperCase() ) );

		return battery;
	}

}
