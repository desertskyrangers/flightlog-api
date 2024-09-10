package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.util.Text;
import com.desertskyrangers.flightdeck.util.Uuid;
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

	private String location;

	private double latitude;

	private double longitude;

	private double altitude;

	private String notes;

	private String userFlightRole;

	private String name;

	private String type;

	private String baseColor;

	private String trimColor;

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
		if( flight.location() != null ) reactFlight.setLocation( flight.location().id().toString() );
		reactFlight.setLatitude( flight.latitude() );
		reactFlight.setLongitude( flight.longitude() );
		reactFlight.setAltitude( flight.altitude() );
		reactFlight.setNotes( flight.notes() );

		// Use aircraft info for the name and type...for now
		if( flight.aircraft() != null ) {
			Aircraft aircraft = flight.aircraft();
			reactFlight.setName( aircraft.name() );
			reactFlight.setType( aircraft.type().name().toLowerCase() );
			if( aircraft.baseColor() != null ) reactFlight.baseColor = aircraft.baseColor().toWeb();
			if( aircraft.trimColor() != null ) reactFlight.trimColor = aircraft.trimColor().toWeb();
		}

		// User flight role
		// TODO Should this move to the core?
		String userFlightRole = "owner";
		if( Objects.equals( requester, flight.observer() ) ) userFlightRole = "observer";
		if( Objects.equals( requester, flight.pilot() ) ) userFlightRole = "pilot";
		reactFlight.setUserFlightRole( userFlightRole );

		return reactFlight;
	}

	public static FlightUpsertRequest toUpsertRequest( ReactFlight reactFlight ) {
		FlightUpsertRequest flight = new FlightUpsertRequest();

		if( "custom".equals( reactFlight.getLocation() ) ) {
			flight.location( Location.CUSTOM_LOCATION_ID );
		} else if( "device".equals( reactFlight.getLocation() ) ) {
			flight.location( Location.DEVICE_LOCATION_ID );
		} else if( "".equals( reactFlight.getLocation() ) ) {
			flight.location( Location.NO_LOCATION_ID );
		} else if( Uuid.isValid( reactFlight.getLocation() ) ) {
			flight.location( UUID.fromString( reactFlight.getLocation() ) );
		} else {
			flight.location( null );
		}

		if( Uuid.isValid( reactFlight.getId() ) ) flight.id( UUID.fromString( reactFlight.getId() ) );
		flight.pilot( UUID.fromString( reactFlight.getPilot() ) );
		flight.unlistedPilot( reactFlight.getUnlistedPilot() );
		flight.observer( UUID.fromString( reactFlight.getObserver() ) );
		flight.unlistedObserver( reactFlight.getUnlistedObserver() );
		flight.aircraft( UUID.fromString( reactFlight.getAircraft() ) );
		flight.batteries( reactFlight.getBatteries() == null ? List.of() : reactFlight.getBatteries().stream().filter( Text::isNotBlank ).map( UUID::fromString ).toList() );
		flight.timestamp( reactFlight.getTimestamp() );
		flight.duration( reactFlight.getDuration() );
		flight.latitude( reactFlight.getLatitude() );
		flight.longitude( reactFlight.getLongitude() );
		flight.altitude( reactFlight.getAltitude() );
		flight.notes( reactFlight.getNotes() );

		return flight;
	}

}
