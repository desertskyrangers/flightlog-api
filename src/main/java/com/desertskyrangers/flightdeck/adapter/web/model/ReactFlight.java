package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.FlightUpsertRequest;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Text;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Accessors( chain = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ReactFlight {

	private String id;

	private String pilot;

	private String unlistedPilot;

	private String observer;

	private String unlistedObserver;

	private String aircraft;

	private List<String> batteries;

	private long timestamp;

	private int duration;

	private String notes;

	private String userFlightRole;

	private String name;

	private String type;

	public static ReactFlight from( User requester, Flight flight ) {
		ReactFlight reactFlight = new ReactFlight();

		reactFlight.setId( flight.id().toString() );
		if( flight.pilot() != null ) reactFlight.setPilot( flight.pilot().id().toString() );
		reactFlight.setUnlistedPilot( flight.unlistedPilot() );
		if( flight.observer() != null ) reactFlight.setObserver( flight.observer().id().toString() );
		reactFlight.setUnlistedObserver( flight.unlistedObserver() );
		if( flight.aircraft() != null ) reactFlight.setAircraft( flight.aircraft().id().toString() );
		if( flight.batteries() != null ) reactFlight.setBatteries( flight.batteries().stream().map( b -> b.id().toString() ).toList() );
		reactFlight.setTimestamp( flight.timestamp() );
		reactFlight.setDuration( flight.duration() );
		reactFlight.setNotes( flight.notes() );

		// Use aircraft info for the name and type...for now
		if( flight.aircraft() != null ) reactFlight.setName( flight.aircraft().name() );
		if( flight.aircraft() != null ) reactFlight.setType( flight.aircraft().type().name().toLowerCase() );

		// User flight role
		// TODO Should this move to the core?
		String userFlightRole = "owner";
		if( Objects.equals( requester, flight.observer() ) ) userFlightRole = "observer";
		if( Objects.equals( requester, flight.pilot() ) ) userFlightRole = "pilot";
		reactFlight.setUserFlightRole( userFlightRole );

		return reactFlight;
	}

	//	public static Flight toFlight( ReactFlight reactFlight ) {
	//		Flight flight = new Flight();
	//
	//		flight.id( UUID.fromString( reactFlight.getId() ) );
	//		// pilot is unable to be set here
	//		// observer is unable to be set here
	//		// aircraft is unable to be set here
	//		// batteries are unable to be set here
	//		flight.timestamp( reactFlight.getTimestamp() );
	//		flight.duration( reactFlight.getDuration() );
	//		flight.notes( reactFlight.getNotes() );
	//
	//		return flight;
	//	}

	public static FlightUpsertRequest toUpsertRequest( ReactFlight flight ) {
		FlightUpsertRequest request = new FlightUpsertRequest();

		request.id( UUID.fromString( flight.getId() ) );
		request.pilot( UUID.fromString( flight.getPilot() ) );
		request.unlistedPilot( flight.getUnlistedPilot() );
		request.observer( UUID.fromString( flight.getObserver() ) );
		request.unlistedObserver( flight.getUnlistedObserver() );
		request.aircraft( UUID.fromString( flight.getAircraft() ) );
		request.batteries( flight.getBatteries() == null ? List.of() : flight.getBatteries().stream().filter( Text::isNotBlank ).map( UUID::fromString ).toList() );
		request.timestamp( flight.getTimestamp() );
		request.duration( flight.getDuration() );
		request.notes( flight.getNotes() );

		return request;
	}

}
