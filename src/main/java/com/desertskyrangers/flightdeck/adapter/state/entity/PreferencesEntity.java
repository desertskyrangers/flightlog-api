package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated Part of an old projection strategy see {@link ProjectionEntity}
 */
public class PreferencesEntity extends HashMap<String, Object> {

	@SuppressWarnings( "unused" )
	public PreferencesEntity() {
		super();
	}

	public PreferencesEntity( Map<? extends String, ?> m ) {
		super( m );
	}

	public static PreferencesProjection from( User user, Map<String, Object> preferences ) {
		PreferencesEntity entity = new PreferencesEntity( preferences );
		return new PreferencesProjection().setId( user.id() ).setJson( Json.stringify( entity ) );
	}

	public static Map<String, Object> toPreferences( PreferencesProjection projection ) {
		return Json.objectify( projection.getJson(), PreferencesEntity.class );
	}

}
