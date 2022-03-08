package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.core.model.PublicDashboard;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Accessors( chain = true )
public class ReactPublicDashboard {

	private String displayName;

	private int pilotFlightCount;

	private long pilotFlightTime;

	private Integer observerFlightCount;

	private Long observerFlightTime;

	private Long lastPilotFlightTimestamp;

	private List<ReactAircraftStats> aircraftStats;

	private List<String> messages;

	public static ReactPublicDashboard from( PublicDashboard dashboard, Map<String, Object> preferences ) {
		boolean showObserverStats = Objects.equals( String.valueOf( preferences.get( "showPublicObserverStats" ) ), "false" );
		boolean showAircraftStats = Objects.equals( String.valueOf( preferences.get( "showPublicAircraftStats" ) ), "false" );

		ReactPublicDashboard reactDashboard = new ReactPublicDashboard();

		reactDashboard.setDisplayName( dashboard.displayName() );
		reactDashboard.setPilotFlightCount( dashboard.flightCount() );
		reactDashboard.setPilotFlightTime( dashboard.flightTime() );

		if( showObserverStats ) {
			reactDashboard.setObserverFlightCount( dashboard.observerCount() );
			reactDashboard.setObserverFlightTime( dashboard.observerTime() );
		}

		reactDashboard.setLastPilotFlightTimestamp( dashboard.lastPilotFlightTimestamp() );

		if( showAircraftStats ) {
			reactDashboard.setAircraftStats( dashboard.aircraftStats().stream().map( ReactAircraftStats::from ).toList() );
		}

		return reactDashboard;
	}

}
