package com.desertskyrangers.flightdeck.adapter.api.model;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

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
		reactFlight.setOwner( flight.owner().toString() );

		return reactFlight;
	}

//	//	public static Flight toFlight( ReactFlight reactFlight ) {
//	//		Flight flight = new Flight();
//	//
//	//		flight.id( UUID.fromString( reactFlight.getId() ) );
//	//		// pilot is unable to be set here
//	//		// observer is unable to be set here
//	//		// aircraft is unable to be set here
//	//		// batteries are unable to be set here
//	//		flight.timestamp( reactFlight.getTimestamp() );
//	//		flight.duration( reactFlight.getDuration() );
//	//		flight.notes( reactFlight.getNotes() );
//	//
//	//		return flight;
//	//	}
//
//	public static FlightUpsertRequest toUpsertRequest( User user, ReactFlight flight ) {
//		FlightUpsertRequest request = new FlightUpsertRequest();
//
//		request.user( user );
//
//		request.id( UUID.fromString( flight.getId() ) );
//		request.pilot( UUID.fromString( flight.getPilot() ) );
//		request.unlistedPilot( flight.getUnlistedPilot() );
//		request.observer( UUID.fromString( flight.getObserver() ) );
//		request.unlistedObserver( flight.getUnlistedObserver() );
//		request.aircraft( UUID.fromString( flight.getAircraft() ) );
//		request.batteries( flight.getBatteries() == null ? List.of() : flight.getBatteries().stream().filter( Text::isNotBlank ).map( UUID::fromString ).toList() );
//		request.timestamp( flight.getTimestamp() );
//		request.duration( flight.getDuration() );
//		request.notes( flight.getNotes() );
//
//		return request;
//	}

}
