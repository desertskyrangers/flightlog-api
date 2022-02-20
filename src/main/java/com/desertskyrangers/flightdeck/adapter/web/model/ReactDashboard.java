package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.core.model.Dashboard;
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

	private List<ReactAircraftStats> aircraftStats;

	public static ReactDashboard from( Dashboard dashboard, Map<String, Object> preferences ) {
		boolean showObserverStats = Objects.equals( String.valueOf( preferences.get( "showObserverStats" ) ), "true" );
		boolean showAircraftStats = Objects.equals( String.valueOf( preferences.get( "showAircraftStats" ) ), "true" );

		ReactDashboard reactDashboard = new ReactDashboard();
		reactDashboard.setPilotFlightCount( dashboard.flightCount() );
		reactDashboard.setPilotFlightTime( dashboard.flightTime() );

		if( showObserverStats ) {
			reactDashboard.setObserverFlightCount( dashboard.observerCount() );
			reactDashboard.setObserverFlightTime( dashboard.observerTime() );
		}

		if( showAircraftStats ) {
			reactDashboard.setAircraftStats( dashboard.aircraftStats().stream().map( ReactAircraftStats::from ).toList() );
		}

		return reactDashboard;
	}

}
