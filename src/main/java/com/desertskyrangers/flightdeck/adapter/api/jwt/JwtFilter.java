package com.desertskyrangers.flightdeck.adapter.api.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filters incoming requests and installs a Spring Security principal
 * if a header corresponding to a valid user is found.
 */
public class JwtFilter extends GenericFilterBean {

	private final JwtTokenProvider tokenProvider;

	public JwtFilter( JwtTokenProvider tokenProvider ) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

		String jwt = JwtToken.resolveToken( httpServletRequest );
		if( StringUtils.hasText( jwt ) && this.tokenProvider.validateToken( jwt ) ) {
			Authentication authentication = this.tokenProvider.getAuthentication( jwt );
			SecurityContextHolder.getContext().setAuthentication( authentication );
		}

		filterChain.doFilter( servletRequest, servletResponse );
	}

}
