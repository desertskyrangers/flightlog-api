package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Flight;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Table( name = "flight" )
public class FlightEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "pilotid", nullable = false, columnDefinition = "BINARY(16)" )
	private UserEntity pilot;

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "observerid", columnDefinition = "BINARY(16)" )
	private UserEntity observer;

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "aircraftid", nullable = false, columnDefinition = "BINARY(16)" )
	private AircraftEntity aircraft;

	@ManyToMany( fetch = FetchType.EAGER )
	@JoinTable( name = "flightbattery", joinColumns = @JoinColumn( name = "flightid" ), inverseJoinColumns = @JoinColumn( name = "batteryid" ) )
	@EqualsAndHashCode.Exclude
	private Set<BatteryEntity> batteries;

	private long timestamp;

	private long duration;

	@Column( length = 1000 )
	private String notes;

	// departure location
	// arrival location

	// weather ?
	// airspace ?

	public static FlightEntity from( Flight flight ) {
		FlightEntity entity = new FlightEntity();

		entity.setId( flight.id() );
		entity.setPilot( UserEntity.from( flight.pilot() ) );
		entity.setObserver( UserEntity.from( flight.observer() ) );
		entity.setAircraft( AircraftEntity.from( flight.aircraft() ) );
		entity.setBatteries( flight.batteries().stream().map( BatteryEntity::from ).collect( Collectors.toSet() ) );
		entity.setTimestamp( flight.timestamp() );
		entity.setDuration( flight.duration() );
		entity.setNotes( flight.notes() );

		return entity;
	}

	public static Flight toFlight( FlightEntity entity ) {
		Flight flight = new Flight();

		flight.id( entity.getId() );
		flight.pilot( UserEntity.toUserAccount( entity.getPilot() ) );
		flight.observer( UserEntity.toUserAccount( entity.getObserver() ) );
		flight.aircraft( AircraftEntity.toAircraft( entity.getAircraft() ) );
		flight.batteries( entity.getBatteries().stream().map( BatteryEntity::toBattery ).collect( Collectors.toSet() ) );
		flight.timestamp( entity.getTimestamp() );
		flight.duration( entity.getDuration() );
		flight.notes( entity.getNotes() );

		return flight;
	}
}
