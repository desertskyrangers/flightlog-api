package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.core.model.Dashboard;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true )
public class ReactDashboard {

	private int pilotFlightCount;

	private long pilotFlightTime;

	public static ReactDashboard from( Dashboard dashboard) {
		return new ReactDashboard().setPilotFlightCount( dashboard.flightCount() ).setPilotFlightTime( dashboard.flightTime() );
	}

}
