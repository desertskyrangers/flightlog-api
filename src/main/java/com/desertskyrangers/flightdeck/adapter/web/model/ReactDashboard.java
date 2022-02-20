package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.core.model.Dashboard;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Objects;

@Data
@Accessors( chain = true )
public class ReactDashboard {

	private Integer pilotFlightCount;

	private Long pilotFlightTime;

	private Integer observerFlightCount;

	private Long observerFlightTime;

	public static ReactDashboard from( Dashboard dashboard, Map<String, Object> preferences ) {
		boolean showObserverStats = Objects.equals( String.valueOf( preferences.get( "showObserverStats" ) ), "true" );
		ReactDashboard reactDashboard = new ReactDashboard();

		reactDashboard.setPilotFlightCount( dashboard.flightCount() );
		reactDashboard.setPilotFlightTime( dashboard.flightTime() );

		if( showObserverStats ) {
			reactDashboard.setObserverFlightCount( dashboard.observerCount() );
			reactDashboard.setObserverFlightTime( dashboard.observerTime() );
		}

		return reactDashboard;
	}

}
