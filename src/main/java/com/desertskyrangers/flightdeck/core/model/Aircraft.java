package com.desertskyrangers.flightdeck.core.model;

import com.desertskyrangers.flightdeck.util.AppColor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Aircraft {

	private UUID id = UUID.randomUUID();

	private String name;

	private AircraftType type;

	private String make;

	private String model;

	private Status status;

	private UUID owner;

	private OwnerType ownerType;

	private double wingspan;

	private double length;

	private double wingarea;

	private double weight;

	private boolean nightLights;

	private int flightCount;

	private long flightTime;

	private AppColor baseColor;

	private AppColor trimColor;

	public enum Status {

		PREFLIGHT( "Pre-flight", true ),
		AIRWORTHY( "Airworthy", true ),
		INOPERATIVE( "Inoperative", false ),
		DECOMMISSIONED( "Decommissioned", false ),
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

}
