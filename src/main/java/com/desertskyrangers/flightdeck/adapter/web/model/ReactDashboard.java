package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.core.model.Dashboard;
import com.desertskyrangers.flightdeck.core.model.PreferenceKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Accessors( chain = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ReactDashboard {

	private Integer pilotFlightCount;

	private Long pilotFlightTime;

	private Integer observerFlightCount;

	private Long observerFlightTime;

	private Long lastPilotFlightTimestamp;

	private List<ReactAircraftStats> aircraftStats;

	public static ReactDashboard from( Dashboard dashboard, Map<String, Object> preferences ) {
		boolean showObserverStats = Objects.equals( String.valueOf( preferences.get( PreferenceKey.SHOW_OBSERVER_STATS ) ), "true" );
		boolean showAircraftStats = Objects.equals( String.valueOf( preferences.get( PreferenceKey.SHOW_AIRCRAFT_STATS ) ), "true" );

		ReactDashboard reactDashboard = new ReactDashboard();
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
