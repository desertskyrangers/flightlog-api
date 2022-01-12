package com.desertskyrangers.flightdeck.adapter.api.model;

import com.desertskyrangers.flightdeck.core.model.Aircraft;
import com.desertskyrangers.flightdeck.core.model.OwnerType;
import com.desertskyrangers.flightdeck.core.model.AircraftStatus;
import com.desertskyrangers.flightdeck.core.model.AircraftType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ReactAircraft {

	private String id;

	private String name;

	private String type;

	private String make;

	private String model;

	private String status;

	private String owner;

	private String ownerType;

	public static ReactAircraft from( Aircraft aircraft ) {
		ReactAircraft result = new ReactAircraft();

		result.setId( aircraft.id().toString() );
		result.setName( aircraft.name() );
		result.setType( aircraft.type().name().toLowerCase() );
		result.setMake( aircraft.make() );
		result.setModel( aircraft.model() );
		result.setStatus( aircraft.status().name().toLowerCase() );
		result.setOwner( aircraft.owner().toString() );
		result.setOwnerType( aircraft.ownerType().name().toLowerCase() );

		return result;
	}

	public static Aircraft toAircraft( ReactAircraft reactAircraft ) {
		Aircraft aircraft = new Aircraft();

		aircraft.id( UUID.fromString( reactAircraft.getId() ) );
		aircraft.name( reactAircraft.getName() );
		aircraft.type( AircraftType.valueOf( reactAircraft.getType().toUpperCase() ) );
		aircraft.make( reactAircraft.getMake() );
		aircraft.model( reactAircraft.getModel() );
		aircraft.status( AircraftStatus.valueOf( reactAircraft.getStatus().toUpperCase() ) );
		if( reactAircraft.getOwner() != null ) aircraft.owner( UUID.fromString( reactAircraft.getOwner() ) );
		if( reactAircraft.getOwnerType() != null ) aircraft.ownerType( OwnerType.valueOf( reactAircraft.getOwnerType().toUpperCase() ) );

		return aircraft;
	}
}

