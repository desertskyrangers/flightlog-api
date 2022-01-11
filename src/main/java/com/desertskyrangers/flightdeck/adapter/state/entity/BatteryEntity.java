package com.desertskyrangers.flightdeck.adapter.state.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "battery" )
public class BatteryEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	private String name;

	private String make;

	private String model;

	private int cycles;

	private int capacity;

	@Column( name = "chargerating" )
	private int chargeRating;

	@Column( name = "dischargerating" )
	private int dischargeRating;

	private String connector;

	private String status;

}
