package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Dashboard;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Json;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true )
public class DashboardEntity {

	private int flightCount;

	private long flightTime;

	public static DashboardProjection from( User user, Dashboard dashboard ) {
		DashboardEntity dashboardEntity = new DashboardEntity();
		dashboardEntity.setFlightCount( dashboard.flightCount() );
		dashboardEntity.setFlightTime( dashboard.flightTime() );
		return new DashboardProjection().setId( user.id() ).setJson( Json.stringify( dashboardEntity ) );
	}

	public static Dashboard toDashboard( DashboardProjection projection ) {
		DashboardEntity entity = Json.objectify( projection.getJson(), DashboardEntity.class );

		Dashboard dashboard = new Dashboard();
		dashboard.flightCount( entity.getFlightCount() );
		dashboard.flightTime( entity.getFlightTime() );

		return dashboard;
	}

}
