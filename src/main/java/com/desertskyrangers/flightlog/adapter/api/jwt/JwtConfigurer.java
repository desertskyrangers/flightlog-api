package com.desertskyrangers.flightlog.adapter.api.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// TODO Can this be moved to WebSecurity???

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private final JwtTokenProvider tokenProvider;

	public JwtConfigurer( JwtTokenProvider tokenProvider ) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void configure( HttpSecurity http ) {
		http.addFilterAfter( new JwtFilter( tokenProvider ), UsernamePasswordAuthenticationFilter.class );
	}

}
