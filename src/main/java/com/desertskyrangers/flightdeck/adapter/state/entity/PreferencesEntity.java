package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Prefs;
import com.desertskyrangers.flightdeck.util.Json;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "preferences" )
@Accessors( chain = true )
public class PreferencesEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( columnDefinition = "VARCHAR2" )
	private String json;

	public static PreferencesEntity from( Prefs preferences ) {
		PreferencesEntity entity = new PreferencesEntity();
		entity.setId( preferences.id() );
		entity.setJson( Json.stringify( preferences.data() ) );
		return entity;
	}

	public static Prefs toPrefs( PreferencesEntity entity ) {
		Prefs preferences = new Prefs();
		preferences.id( entity.getId() );
		preferences.data( Json.asMap( entity.getJson() ) );
		return preferences;
	}

}
