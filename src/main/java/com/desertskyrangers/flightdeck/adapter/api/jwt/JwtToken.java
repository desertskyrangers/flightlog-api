package com.desertskyrangers.flightdeck.adapter.api.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class JwtToken {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static final String AUTHORIZATION_TYPE = "Bearer";

	public static final String USER_ID_CLAIM_KEY = "uid";

	static final String SUBJECT_CLAIM_KEY = "sub";

	static final String AUTHORITIES_CLAIM_KEY = "auth";

	static final String EXPIRES_CLAIM_KEY = "exp";

	private String token;

	public JwtToken( String token ) {
		this.token = token;
	}

	@JsonProperty( "token" )
	public String getToken() {
		return token;
	}

	public void setToken( String token ) {
		this.token = token;
	}

	public static String resolveToken( HttpServletRequest request ) {
		String bearerToken = request.getHeader( AUTHORIZATION_HEADER );
		if( StringUtils.hasText( bearerToken ) && bearerToken.startsWith( AUTHORIZATION_TYPE ) ) return bearerToken.substring( AUTHORIZATION_TYPE.length() + 1 );
		return null;
	}

}
