package com.desertskyrangers.flightdeck.adapter.state.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "publicdashboard" )
@Accessors( chain = true )
public class PublicDashboardProjection {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( columnDefinition = "TEXT" )
	private String json;

}
