package com.desertskyrangers.flightlog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Json {

	public static String stringify( Object object ) throws ResponseStatusException {
		try {
			return new ObjectMapper().writeValueAsString( object );
		} catch( JsonProcessingException exception ) {
			throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception );
		}
	}

}
