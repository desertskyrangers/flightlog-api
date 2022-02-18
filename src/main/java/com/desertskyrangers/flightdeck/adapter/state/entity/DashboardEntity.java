package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Dashboard;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Json;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table( name = "dashboard" )
@Accessors( chain = true )
public class DashboardEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( columnDefinition = "TEXT" )
	private String json;

	public static DashboardEntity from( User user, Dashboard dashboard ) {
		DashboardEntity dashboardEntity = new DashboardEntity();
		dashboardEntity.setId( user.id() );
		dashboardEntity.setJson( Json.stringify( dashboard ) );
		return dashboardEntity;
	}

	public static Dashboard toDashboard( DashboardEntity dashboardEntity ) {
		Map<String, Object> dashboardMap = Json.asMap( dashboardEntity.getJson() );
		Dashboard dashboard = new Dashboard();
		dashboard.id( dashboardEntity.getId() );
		dashboard.flightTime( (long)dashboardMap.get( "flightTime" ) );
		dashboard.flightCount( (int)dashboardMap.get( "flightCount" ) );
		return dashboard;
	}
}
