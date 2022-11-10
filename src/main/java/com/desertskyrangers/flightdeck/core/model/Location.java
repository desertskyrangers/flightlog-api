package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Location {

	// The average radius of the Earth in meters
	// https://en.wikipedia.org/wiki/Earth_radius
	private static final double rE = 6.3781e6;

	private static final double DEGREES_PER_RADIAN = 180.0 / Math.PI;

	private static final double RADIANS_PER_DEGREE = Math.PI / 180.0;

	private UUID id = UUID.randomUUID();

	// Latitude in decimal degrees
	private double latitude;

	// Longitude in decimal degrees
	private double longitude;

	/**
	 * The name of the location.
	 */
	private String name;

	/**
	 * The diameter of the location in meters.
	 */
	private double radius;

	public boolean containsByRadius( double latitude, double longitude ) {
		double dx = latitude - latitude();
		double dy = longitude - longitude();
		double offset = Math.sqrt( dx * dx + dy * dy );
		return degreesToMetersOnEarth( offset ) <= radius;
	}

	public boolean containsBySquare( double latitude, double longitude ) {
		double offset = metersToDegreesOnEarth( radius() );
		double x1 = latitude() - offset;
		double x2 = latitude() + offset;
		double y1 = longitude() - offset;
		double y2 = longitude() + offset;

		boolean latitudeContained = latitude >= x1 && latitude <= x2;
		boolean longitudeContained = longitude >= y1 && longitude <= y2;

		return latitudeContained && longitudeContained;
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

}
