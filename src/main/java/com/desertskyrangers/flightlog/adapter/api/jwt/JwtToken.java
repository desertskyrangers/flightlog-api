package com.desertskyrangers.flightlog.adapter.api.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class JwtToken {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static final String AUTHORIZATION_TYPE = "Bearer";

	private String token;

	public JwtToken( String token ) {
		this.token = token;
	}

	@JsonProperty( "token" )
	String getToken() {
		return token;
	}

	void setToken( String token ) {
		this.token = token;
	}

	public static String resolveToken( HttpServletRequest request ) {
		String bearerToken = request.getHeader( AUTHORIZATION_HEADER );
		if( StringUtils.hasText( bearerToken ) && bearerToken.startsWith( AUTHORIZATION_TYPE ) ) return bearerToken.substring( AUTHORIZATION_TYPE.length() + 1 );
		return null;
	}

}
