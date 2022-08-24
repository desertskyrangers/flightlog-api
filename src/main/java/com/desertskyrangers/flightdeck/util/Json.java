package com.desertskyrangers.flightdeck.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json {

	public static String stringify( Object object ) throws ResponseStatusException {
		try {
			return new ObjectMapper().enable( SerializationFeature.EAGER_SERIALIZER_FETCH ).writeValueAsString( object );
		} catch( JsonProcessingException exception ) {
			throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception );
		}
	}

	public static <T> T objectify( String json, Class<T> type ) {
		try {
			return new ObjectMapper().readValue( json, type );
		} catch( JsonProcessingException exception ) {
			throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception );
		}
	}

	@Deprecated
	public static Map<String, Object> asMap( Object object ) {
		return asMap( stringify( object ) );
	}

	public static Map<String, Object> asMap( String json ) {
		TypeReference<HashMap<String, Object>> type = new TypeReference<>() {};
		try {
			return new ObjectMapper().readValue( json, type );
		} catch( JsonProcessingException exception ) {
			throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception );
		}
	}

	public static List<Object> asList( String json ) {
		TypeReference<ArrayList<Object>> type = new TypeReference<>() {};
		try {
			return new ObjectMapper().readValue( json, type );
		} catch( JsonProcessingException exception ) {
			throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception );
		}
	}

}
