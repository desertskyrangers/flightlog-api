package com.desertskyrangers.flightdeck.adapter.state.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "aircraft" )
public class FlightEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;



}
