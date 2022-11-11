package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.core.model.Location;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ReactLocation {

	private String id;

	private double latitude;

	private double longitude;

	private String name;

	private double size;

	public static ReactLocation from( Location location ) {
		ReactLocation result = new ReactLocation();

		result.setId( location.id().toString() );
		result.setLatitude( location.latitude() );
		result.setLongitude( location.longitude() );
		result.setName( location.name() );
		result.setSize( location.size() );

		return result;
	}

	public static Location toLocation( ReactLocation location ) {
		Location result = new Location();

		result.id( UUID.fromString( location.getId() ) );
		result.latitude( location.getLatitude() );
		result.longitude( location.getLongitude() );
		result.name( location.getName() );
		result.size( location.getSize() );

		return result;
	}

}
