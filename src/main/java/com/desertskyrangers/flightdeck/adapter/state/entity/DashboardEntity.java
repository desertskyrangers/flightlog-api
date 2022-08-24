package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Dashboard;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Json;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @deprecated Part of an old projection strategy see {@link ProjectionEntity}
 */
@Data
@Accessors( chain = true )
public class DashboardEntity {

	private int flightCount;

	private long flightTime;

	private int observerCount;

	private long observerTime;

	private long lastPilotFlightTimestamp;

	private List<AircraftStatsEntity> aircraftStats;

	public static DashboardProjection from( User user, Dashboard dashboard ) {
		DashboardEntity dashboardEntity = new DashboardEntity();
		dashboardEntity.setFlightCount( dashboard.flightCount() );
		dashboardEntity.setFlightTime( dashboard.flightTime() );
		dashboardEntity.setObserverCount( dashboard.observerCount() );
		dashboardEntity.setObserverTime( dashboard.observerTime() );
		dashboardEntity.setAircraftStats( dashboard.aircraftStats().stream().map( AircraftStatsEntity::from ).toList() );
		dashboardEntity.setLastPilotFlightTimestamp( dashboard.lastPilotFlightTimestamp() );
		return new DashboardProjection().setId( user.id() ).setJson( Json.stringify( dashboardEntity ) );
	}

	public static Dashboard toDashboard( DashboardProjection projection ) {
		DashboardEntity entity = Json.objectify( projection.getJson(), DashboardEntity.class );

		Dashboard dashboard = new Dashboard();
		dashboard.flightCount( entity.getFlightCount() );
		dashboard.flightTime( entity.getFlightTime() );
		dashboard.observerCount( entity.getObserverCount() );
		dashboard.observerTime( entity.getObserverTime() );
		dashboard.lastPilotFlightTimestamp( entity.getLastPilotFlightTimestamp() );
		dashboard.aircraftStats( entity.getAircraftStats().stream().map( AircraftStatsEntity::toAircraftStats ).toList() );

		return dashboard;
	}

}
