package com.desertskyrangers.flightdeck.adapter.state.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table( name = "flight" )
public class FlightEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "aircraftid", nullable = false, updatable = false, columnDefinition = "BINARY(16)" )
	@EqualsAndHashCode.Exclude
	private AircraftEntity aircraft;

	@ManyToMany( fetch = FetchType.EAGER )
	@JoinTable( name = "flightbattery", joinColumns = @JoinColumn( name = "flightid" ), inverseJoinColumns = @JoinColumn( name = "batteryid" ) )
	@EqualsAndHashCode.Exclude
	private Set<BatteryEntity> batteries;

	private long timestamp;

	private long duration;

	// location
	// notes

	// weather ?
	// airspace ?

}
