package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.jwt.JwtFilter;
import com.desertskyrangers.flightlog.adapter.api.jwt.JwtTokenProvider;
import com.desertskyrangers.flightlog.core.AppPrincipalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final AppPrincipalService appPrincipalService;

	private final JwtTokenProvider jwtTokenProvider;

	public WebSecurity( AppPrincipalService appPrincipalService, JwtTokenProvider jwtTokenProvider ) {
		this.appPrincipalService = appPrincipalService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	// Expose the AuthenticationManager bean
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure( AuthenticationManagerBuilder builder ) throws Exception {
		builder.userDetailsService( appPrincipalService ).passwordEncoder( passwordEncoder() );
	}

	@Override
	protected void configure( HttpSecurity http ) throws Exception {
		// @formatter:off
		http
			.cors().and()
			.csrf().ignoringAntMatchers( ApiPath.AUTH_LOGIN ).and()
			.csrf().ignoringAntMatchers( ApiPath.AUTH_REGISTER ).and()
			.authorizeRequests()
				.mvcMatchers( HttpMethod.POST, ApiPath.AUTH_LOGIN ).permitAll()
				.mvcMatchers( HttpMethod.POST, ApiPath.AUTH_REGISTER ).permitAll()
				.mvcMatchers( HttpMethod.GET, ApiPath.AUTH_VERIFY ).permitAll()
				.mvcMatchers( HttpMethod.GET, ApiPath.MONITOR_STATUS ).permitAll()
				.anyRequest().authenticated()
			.and().addFilterAfter( new JwtFilter( jwtTokenProvider ), UsernamePasswordAuthenticationFilter.class );
		// @formatter:on
	}

}
