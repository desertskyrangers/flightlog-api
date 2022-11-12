package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Flight;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.core.model.LocationStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "location" )
@Accessors( chain = true )
public class LocationEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	private double latitude;

	private double longitude;

	@Column( length = 160 )
	private String name;

	private double size;

	private String status;

	public static LocationEntity from( Location location ) {
		LocationEntity entity = new LocationEntity();

		entity.setId( location.id() );
		entity.setLatitude( location.latitude() );
		entity.setLongitude( location.longitude() );
		entity.setName( location.name() );
		entity.setSize( location.size() );
		entity.setStatus( location.status().name().toLowerCase() );

		return entity;
	}

	public static Location toLocation( LocationEntity entity ) {
		Location location = new Location();

		location.id( entity.getId() );
		location.latitude( entity.getLatitude() );
		location.longitude( entity.getLongitude() );
		location.name( entity.getName() );
		location.size( entity.getSize() );
		location.status( LocationStatus.valueOf( entity.getStatus().toUpperCase() ) );

		return location;
	}

}
