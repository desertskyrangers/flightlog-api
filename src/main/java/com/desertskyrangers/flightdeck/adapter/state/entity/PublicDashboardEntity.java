package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.PublicDashboard;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Json;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( chain = true )
public class PublicDashboardEntity {

	private String displayName;

	private int flightCount;

	private long flightTime;

	private int observerCount;

	private long observerTime;

	private long lastPilotFlightTimestamp;

	private List<AircraftStatsEntity> aircraftStats;

	public static PublicDashboardProjection from( User user, PublicDashboard dashboard ) {
		PublicDashboardEntity dashboardEntity = new PublicDashboardEntity();
		dashboardEntity.setDisplayName( dashboard.displayName() );
		dashboardEntity.setFlightCount( dashboard.flightCount() );
		dashboardEntity.setFlightTime( dashboard.flightTime() );
		dashboardEntity.setObserverCount( dashboard.observerCount() );
		dashboardEntity.setObserverTime( dashboard.observerTime() );
		dashboardEntity.setAircraftStats( dashboard.aircraftStats().stream().map( AircraftStatsEntity::from ).toList() );
		dashboardEntity.setLastPilotFlightTimestamp( dashboard.lastPilotFlightTimestamp() );
		return new PublicDashboardProjection().setId( user.id() ).setJson( Json.stringify( dashboardEntity ) );
	}

	public static PublicDashboard toDashboard( PublicDashboardProjection projection ) {
		PublicDashboardEntity entity = Json.objectify( projection.getJson(), PublicDashboardEntity.class );

		PublicDashboard dashboard = new PublicDashboard();
		dashboard.displayName( entity.getDisplayName() );
		dashboard.flightCount( entity.getFlightCount() );
		dashboard.flightTime( entity.getFlightTime() );
		dashboard.observerCount( entity.getObserverCount() );
		dashboard.observerTime( entity.getObserverTime() );
		dashboard.lastPilotFlightTimestamp( entity.getLastPilotFlightTimestamp() );
		dashboard.aircraftStats( entity.getAircraftStats().stream().map( AircraftStatsEntity::toAircraftStats ).toList() );

		return dashboard;
	}

}
