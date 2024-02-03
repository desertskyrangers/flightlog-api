package com.desertskyrangers.flightdeck.adapter.store.entity;

import com.desertskyrangers.flightdeck.core.model.Flight;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Table( name = "flight" )
@Accessors( chain = true )
public class FlightEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "pilotid", nullable = false, columnDefinition = "BINARY(16)" )
	private UserEntity pilot;

	@Column( name = "unlistedpilot" )
	private String unlistedPilot;

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "observerid", columnDefinition = "BINARY(16)" )
	private UserEntity observer;

	@Column( name = "unlistedobserver" )
	private String unlistedObserver;

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "aircraftid", nullable = false, columnDefinition = "BINARY(16)" )
	private AircraftEntity aircraft;

	@ManyToMany( fetch = FetchType.EAGER )
	@JoinTable( name = "flightbattery", joinColumns = @JoinColumn( name = "flightid" ), inverseJoinColumns = @JoinColumn( name = "batteryid" ) )
	@EqualsAndHashCode.Exclude
	private Set<BatteryEntity> batteries;

	private long timestamp;

	private int duration;

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "locationid", nullable = true, columnDefinition = "BINARY(16)" )
	private LocationEntity location;

	private double latitude;

	private double longitude;

	@Column( length = 1000 )
	private String notes;

	// departure location
	// arrival location

	// weather ?
	// airspace ?

	public static FlightEntity from( Flight flight ) {
		FlightEntity entity = new FlightEntity();

		entity.setId( flight.id() );
		if( flight.pilot() != null ) entity.setPilot( UserEntity.from( flight.pilot() ) );
		entity.setUnlistedPilot( flight.unlistedPilot() );
		if( flight.observer() != null ) entity.setObserver( UserEntity.from( flight.observer() ) );
		entity.setUnlistedObserver( flight.unlistedObserver() );
		if( flight.aircraft() != null ) entity.setAircraft( AircraftEntity.from( flight.aircraft() ) );
		if( flight.batteries() != null ) entity.setBatteries( flight.batteries().stream().map( BatteryEntity::from ).collect( Collectors.toSet() ) );
		entity.setTimestamp( flight.timestamp() );
		entity.setDuration( flight.duration() );
		if( flight.location() != null ) entity.setLocation( LocationEntity.from( flight.location() ) );
		entity.setLatitude( flight.latitude() );
		entity.setLongitude( flight.longitude() );
		entity.setNotes( flight.notes() );

		return entity;
	}

	public static Flight toFlight( FlightEntity entity ) {
		Flight flight = new Flight();

		flight.id( entity.getId() );
		if( entity.getPilot() != null ) flight.pilot( UserEntity.toUser( entity.getPilot() ) );
		flight.unlistedPilot( entity.getUnlistedPilot() );
		if( entity.getObserver() != null ) flight.observer( UserEntity.toUser( entity.getObserver() ) );
		flight.unlistedObserver( entity.getUnlistedObserver() );
		if( entity.getAircraft() != null ) flight.aircraft( AircraftEntity.toAircraft( entity.getAircraft() ) );
		if( entity.getBatteries() != null ) flight.batteries( entity.getBatteries().stream().map( BatteryEntity::toBattery ).collect( Collectors.toSet() ) );
		flight.timestamp( entity.getTimestamp() );
		flight.duration( entity.getDuration() );
		if( entity.getLocation() != null ) flight.location( LocationEntity.toLocation( entity.getLocation() ) );
		flight.latitude( entity.getLatitude() );
		flight.longitude( entity.getLongitude() );
		flight.notes( entity.getNotes() );

		return flight;
	}

}
