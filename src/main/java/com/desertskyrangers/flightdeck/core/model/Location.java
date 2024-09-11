package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Location {

	public static final UUID CUSTOM_LOCATION_ID = UUID.fromString( "b9902649-9a9f-4d96-a219-3f2a52c1357e" );

	public static final UUID DEVICE_LOCATION_ID = UUID.fromString( "313d9657-abbd-4f5c-a9c3-9618a2574e18" );

	public static final UUID NO_LOCATION_ID = UUID.fromString( "a65a59cb-45f5-43c0-94c8-d6489d1ca19f" );

	public static final Location CUSTOM_LOCATION = new Location().id( CUSTOM_LOCATION_ID ).name( "Custom Location" ).user( User.INTERNAL_OWNER ).status( Status.ACTIVE );

	public static final Location DEVICE_LOCATION = new Location().id( DEVICE_LOCATION_ID ).name( "Device Location" ).user( User.INTERNAL_OWNER ).status( Status.ACTIVE );

	public static final Location NO_LOCATION = new Location().id( NO_LOCATION_ID ).name( "No Location" ).user( User.INTERNAL_OWNER ).status( Status.ACTIVE );

	public static final double DEFAULT_LOCATION_SIZE = 100;

	public static final Status DEFAULT_LOCATION_STATUS = Status.ACTIVE;

	// The average radius of the Earth in meters
	// https://en.wikipedia.org/wiki/Earth_radius
	private static final double rE = 6.3781e6;

	private static final double DEGREES_PER_RADIAN = 180.0 / Math.PI;

	private static final double RADIANS_PER_DEGREE = Math.PI / 180.0;

	private static final Map<UUID, Location> specialLocations;

	private UUID id = UUID.randomUUID();

	// Latitude in decimal degrees
	private double latitude;

	// Longitude in decimal degrees
	private double longitude;

	// Altitude in meters
	private double altitude;

	private User user;

	/**
	 * The name of the location.
	 */
	private String name;

	/**
	 * The size of the location in meters. This is currently interpreted as a
	 * radius around the location that covers the location.
	 */
	private double size = DEFAULT_LOCATION_SIZE;

	private Status status = DEFAULT_LOCATION_STATUS;

	static {
		specialLocations = Map.of( CUSTOM_LOCATION_ID, CUSTOM_LOCATION, DEVICE_LOCATION_ID, DEVICE_LOCATION, NO_LOCATION_ID, NO_LOCATION );
	}

	// TODO Can locations have types? Which ones?

	public boolean contains( Location location ) {
		return contains( location.latitude(), location.longitude() );
	}

	public boolean contains( double latitude, double longitude ) {
		return containsByDiameter( latitude, longitude );
	}

	public boolean containsByDiameter( double latitude, double longitude ) {
		return containedByRadius( latitude, longitude, 0.5 * size );
	}

	public boolean containsByRadius( double latitude, double longitude ) {
		return containedByRadius( latitude, longitude, size );
	}

	public boolean containsBySquare( double latitude, double longitude ) {
		double offset = metersToDegreesOnEarth( size() );
		double x1 = latitude() - offset;
		double x2 = latitude() + offset;
		double y1 = longitude() - offset;
		double y2 = longitude() + offset;

		boolean latitudeContained = latitude >= x1 && latitude <= x2;
		boolean longitudeContained = longitude >= y1 && longitude <= y2;

		return latitudeContained && longitudeContained;
	}

	private boolean containedByRadius( double latitude, double longitude, double size ) {
		double dx = latitude - latitude();
		double dy = longitude - longitude();
		double offset = Math.sqrt( dx * dx + dy * dy );
		return degreesToMetersOnEarth( offset ) <= size;
	}

	/**
	 * Convert a distance in meters to decimal degrees on the surface of the
	 * Earth. This method reasonably accurate up to distances of a few kilometers.
	 *
	 * @param distance A distance in meters
	 * @return The decimal degree distance
	 */
	public static double metersToDegreesOnEarth( double distance ) {
		return Math.atan2( distance, rE ) * DEGREES_PER_RADIAN;
	}

	/**
	 * Convert a distance in decimal degrees to meters on the surface of the
	 * Earth. This method reasonably accurate up to distances of a few degrees.
	 *
	 * @param distance A distance in decimal degrees
	 * @return The distance in meters
	 */
	public static double degreesToMetersOnEarth( double distance ) {
		return Math.sin( distance * RADIANS_PER_DEGREE ) * rE;
	}

	public static Location forId( UUID id ) {
		if( id == null ) return null;
		return specialLocations.get( id );
	}

	public enum Status {

		ACTIVE,

		REMOVED;

		public static boolean isValid( String string ) {
			try {
				valueOf( string.toUpperCase() );
				return true;
			} catch( NullPointerException | IllegalArgumentException exception ) {
				return false;
			}
		}

		public static boolean isNotValid( String string ) {
			return !isValid( string );
		}

	}

}
