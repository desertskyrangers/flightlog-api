package com.desertskyrangers.flightdeck.adapter.state.entity;

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
@Table( name = "preferences" )
@Accessors( chain = true )
public class PreferencesEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( columnDefinition = "TEXT" )
	private String json;

	public static PreferencesEntity from( User user, Map<String,Object> preferences ) {
		PreferencesEntity entity = new PreferencesEntity();
		entity.setId( user.id() );
		entity.setJson( Json.stringify( preferences ) );
		return entity;
	}

	public static Map<String,Object> toPreferences( PreferencesEntity entity ) {
		return Json.asMap( entity.getJson() );
	}

}
