package com.desertskyrangers.flightdeck.adapter.api.model;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.GroupType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ReactGroup {

	private String id;

	private String type;

	private String name;

	private String owner;

	public static ReactGroup from( Group flight ) {
		ReactGroup reactFlight = new ReactGroup();

		reactFlight.setId( flight.id().toString() );
		reactFlight.setType( flight.type().name().toLowerCase() );
		reactFlight.setName( flight.name() );
		reactFlight.setOwner( flight.owner().id().toString() );

		return reactFlight;
	}

	public static Group toGroup( ReactGroup reactGroup ) {
		Group group = new Group();

		group.id( UUID.fromString( reactGroup.getId() ) );
		group.type( GroupType.valueOf( reactGroup.getType().toUpperCase() ) );
		group.name( reactGroup.getName() );
		// Owner unable to be set here

		return group;
	}

}
