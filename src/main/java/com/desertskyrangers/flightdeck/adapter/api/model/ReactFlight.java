package com.desertskyrangers.flightdeck.adapter.api.model;

import com.desertskyrangers.flightdeck.core.model.Flight;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors( chain = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ReactFlight {

	private String id;

	private String pilot;

	private String observer;

	private String aircraft;

	private List<String> batteries;

	private long timestamp;

	private long duration;

	private String notes;

	public static ReactFlight from( Flight flight ) {
		ReactFlight reactFlight = new ReactFlight();

		reactFlight.setId( flight.id().toString() );
		if( flight.pilot() != null ) reactFlight.setPilot( flight.pilot().id().toString() );
		if( flight.observer() != null ) reactFlight.setObserver( flight.observer().id().toString() );
		if( flight.aircraft() != null ) reactFlight.setAircraft( flight.aircraft().id().toString() );
		if( flight.batteries() != null ) reactFlight.setBatteries( flight.batteries().stream().map( b -> b.id().toString() ).toList() );
		reactFlight.setTimestamp( flight.timestamp() );
		reactFlight.setDuration( flight.duration() );
		reactFlight.setNotes( flight.notes() );

		return reactFlight;
	}

	public static Flight toFlight( ReactFlight reactFlight ) {
		Flight flight = new Flight();

		flight.id( UUID.fromString( reactFlight.getId() ) );
		// pilot is unable to be set here
		// observer is unable to be set here
		// aircraft is unable to be set here
		// batteries are unable to be set here
		flight.timestamp( reactFlight.getTimestamp() );
		flight.duration( reactFlight.getDuration() );
		flight.notes( reactFlight.getNotes() );

		return flight;
	}
}
