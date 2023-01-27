package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Battery {

	public static final int MAX_CYCLES = 200;

	private UUID id = UUID.randomUUID();

	private String name;

	private String make;

	private String model;

	private Connector connector;

	private String unlistedConnector;

	private Status status;

	private Chemistry chemistry;

	private int cells;

	private int initialCycles;

	private int cycles;

	private int capacity;

	private int dischargeRating;

	private UUID owner;

	private OwnerType ownerType;

	private int flightCount;

	private long flightTime;

	public int life() {
		return (int)((100D * (Battery.MAX_CYCLES - cycles()) / Battery.MAX_CYCLES));
	}

	public enum Connector {

		XT30( "XT30" ),
		XT60( "XT60" ),
		XT90( "XT90" ),
		DEANS( "Deans T Plug" ),
		DEANS_MINI( "Deans Mini" ),
		EC2( "EC2" ),
		EC3( "EC3" ),
		EC5( "EC5" ),
		JST( "JST" ),
		MOLEX( "Molex" ),
		SERVO( "Servo" ),
		TRX( "TRX" ),
		UNLISTED("Unlisted");

		private final String name;

		Connector( String name ) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

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

	public enum Status {

		NEW( "New", true ),
		AVAILABLE( "Available", true ),
		DESTROYED( "Destroyed", false );

		private final String name;

		private final boolean airworthy;

		Status( String name, boolean airworthy ) {
			this.name = name;
			this.airworthy = airworthy;
		}

		public String getName() {
			return name;
		}

		public boolean isAirworthy() {
			return airworthy;
		}

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

	public enum Chemistry {

		NICD( "NiCd", 1.2D ),
		LIPO( "LiPo", 3.7D ),
		LIFE( "LiFe", 3.3D ),
		NIMH( "NiMH", 1.2D ),
		NIZN( "NiZn", 1.6D ),
		LEAD( "Lead", 2.0D );

		private final String name;

		private final double voltsPerCell;

		Chemistry( String name, double voltsPerCell ) {
			this.name = name;
			this.voltsPerCell = voltsPerCell;
		}

		public String getName() {
			return name;
		}

		public double getVoltsPerCell() {
			return voltsPerCell;
		}

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
