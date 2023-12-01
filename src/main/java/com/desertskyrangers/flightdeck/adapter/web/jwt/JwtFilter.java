package com.desertskyrangers.flightdeck.adapter.web.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filters incoming requests and installs a Spring Security principal
 * if a header corresponding to a valid user is found.
 */
@Slf4j
public class JwtFilter extends GenericFilterBean {

	private final JwtTokenProvider tokenProvider;

	public JwtFilter( JwtTokenProvider tokenProvider ) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException {
		String jwt = JwtToken.resolveToken( (HttpServletRequest)servletRequest );

		if( tokenProvider.validateToken( jwt ) ) {
			Authentication authentication = tokenProvider.getAuthentication( jwt );
			SecurityContextHolder.getContext().setAuthentication( authentication );
			//log.info( "authenticated=" + authentication.getPrincipal() );
		}

		filterChain.doFilter( servletRequest, servletResponse );
	}

}
