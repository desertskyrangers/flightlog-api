package com.desertskyrangers.flightdeck.adapter.web.jwt;

import com.desertskyrangers.flightdeck.core.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

	private Key key;

	private long tokenValidityInMilliseconds;

	private long tokenValidityInMillisecondsForRememberMe;

	/**
	 * Base-64 encoded secret.
	 */
	@Value( "${security.authentication.jwt.secret}" )
	private String jwtSecret;

	@Value( "${security.authentication.jwt.token-validity-in-seconds}" )
	private Integer jwtTokenValidityInSeconds;

	@Value( "${security.authentication.jwt.token-validity-in-seconds-for-remember-me}" )
	private Integer jwtRememberedTokenValidityInSeconds;

	public JwtTokenProvider() {}

	@PostConstruct
	public void init() {
		byte[] keyBytes = Decoders.BASE64.decode( jwtSecret );
		this.key = Keys.hmacShaKeyFor( keyBytes );
		this.tokenValidityInMilliseconds = 1000L * jwtTokenValidityInSeconds;
		this.tokenValidityInMillisecondsForRememberMe = 1000L * jwtRememberedTokenValidityInSeconds;
	}

	public String createToken( User account, Authentication authentication, boolean remember ) {
		return createToken( account, authentication, remember, System.currentTimeMillis() );
	}

	public Map<String, Object> parse( String token ) {
		return Jwts.parserBuilder().setSigningKey( key ).build().parseClaimsJws( token ).getBody();
	}

	public Authentication getAuthentication( String token ) {
		Claims claims = Jwts.parserBuilder().setSigningKey( key ).build().parseClaimsJws( token ).getBody();

		Collection<? extends GrantedAuthority> authorities = Arrays
			.stream( String.valueOf( claims.get( JwtToken.AUTHORITIES_CLAIM_KEY ) ).split( "," ) )
			.map( SimpleGrantedAuthority::new )
			.collect( Collectors.toList() );

		org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User( claims.getSubject(), "", authorities );

		return new UsernamePasswordAuthenticationToken( principal, token, authorities );
	}

	public String getUserId( String token ) {
		return String.valueOf( parse( token).get( JwtToken.USER_ID_CLAIM_KEY ) );
	}

	public boolean validateToken( String token ) {
		try {
			Jwts.parserBuilder().setSigningKey( key ).build().parse( token );
			return true;
		} catch( io.jsonwebtoken.security.SecurityException | MalformedJwtException exception ) {
			log.info( "Invalid JWT signature." );
			log.trace( "Invalid JWT signature trace: {}", exception.getMessage(), exception );
		} catch( ExpiredJwtException exception ) {
			log.info( "Expired JWT token." );
			log.trace( "Expired JWT token trace: {}", exception.getMessage(), exception );
		} catch( UnsupportedJwtException exception ) {
			log.info( "Unsupported JWT token." );
			log.trace( "Unsupported JWT token trace: {}", exception.getMessage(), exception );
		} catch( IllegalArgumentException exception ) {
			log.info( "JWT token compact of handler are invalid." );
			log.trace( "JWT token compact of handler are invalid trace: {}", exception.getMessage(), exception );
		}
		return false;
	}

	int getJwtValidityInSeconds() {
		return jwtTokenValidityInSeconds;
	}

	int getRememberedJwtValidityInSeconds() {
		return jwtRememberedTokenValidityInSeconds;
	}

	String createToken( User user, Authentication authentication, boolean remember, long timestamp ) {
		String userId = user.id().toString();
		String subject = authentication.getName();
		String authorities = authentication.getAuthorities().stream().map( GrantedAuthority::getAuthority ).collect( Collectors.joining( "," ) );

		Date validity = new Date( timestamp + tokenValidityInMilliseconds );
		if( remember ) validity = new Date( timestamp + tokenValidityInMillisecondsForRememberMe );

		String token = Jwts
			.builder()
			.claim( JwtToken.USER_ID_CLAIM_KEY, userId )
			.setSubject( subject )
			.claim( JwtToken.AUTHORITIES_CLAIM_KEY, authorities )
			.setExpiration( validity )
			.signWith( key, SignatureAlgorithm.HS512 )
			.compact();

		// TODO Potentially store the tokens?

		return token;
	}

}
