package com.desertskyrangers.flightdeck.adapter.api;

import com.desertskyrangers.flightdeck.adapter.api.jwt.JwtFilter;
import com.desertskyrangers.flightdeck.adapter.api.jwt.JwtTokenProvider;
import com.desertskyrangers.flightdeck.core.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebBaseSecurity extends WebSecurityConfigurerAdapter {

	private final AppUserDetailsService appUserDetailsService;

	private final JwtTokenProvider jwtTokenProvider;

	public WebBaseSecurity( AppUserDetailsService appUserDetailsService, JwtTokenProvider jwtTokenProvider ) {
		this.appUserDetailsService = appUserDetailsService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public UserDetailsService userDetailsServiceBean() {
		return appUserDetailsService;
	}

	@Override
	protected void configure( AuthenticationManagerBuilder builder ) throws Exception {
		builder.userDetailsService( appUserDetailsService ).passwordEncoder( passwordEncoder() );
	}

	@Override
	protected void configure( HttpSecurity http ) throws Exception {
		// @formatter:off
		http
			.cors()
			.and().csrf().disable()
			.authorizeRequests()
				.mvcMatchers( HttpMethod.POST, ApiPath.AUTH_RECOVER ).permitAll()
				.mvcMatchers( HttpMethod.POST, ApiPath.AUTH_REGISTER ).permitAll()
				.antMatchers( HttpMethod.POST, ApiPath.AUTH_RESEND ).permitAll()
				.antMatchers( HttpMethod.POST, ApiPath.AUTH_RESET ).permitAll()
				.antMatchers( HttpMethod.POST, ApiPath.AUTH_VERIFY ).permitAll()
				.mvcMatchers( HttpMethod.POST, ApiPath.AUTH_LOGIN ).permitAll()
				.mvcMatchers( HttpMethod.POST, ApiPath.AUTH_LOGOUT ).permitAll()
				.mvcMatchers( HttpMethod.GET, ApiPath.MONITOR_STATUS ).permitAll()
				.anyRequest().authenticated()
			.and()
				.addFilterAfter( new JwtFilter( jwtTokenProvider ), UsernamePasswordAuthenticationFilter.class );
		// @formatter:on
	}

}
