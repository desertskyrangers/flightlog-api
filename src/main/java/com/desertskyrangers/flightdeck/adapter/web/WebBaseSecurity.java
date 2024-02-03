package com.desertskyrangers.flightdeck.adapter.web;

import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtFilter;
import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtTokenProvider;
import com.desertskyrangers.flightdeck.core.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebBaseSecurity {

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
	public UserDetailsService userDetailsServiceBean() {
		return appUserDetailsService;
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService( appUserDetailsService );
		authenticationProvider.setPasswordEncoder( passwordEncoder() );
		return authenticationProvider;
	}

	@Bean
	public SecurityFilterChain configure( HttpSecurity http ) throws Exception {
		// @formatter:off
		return http
			.csrf( AbstractHttpConfigurer::disable )
			.authorizeHttpRequests( requests -> requests
				.requestMatchers( AntPathRequestMatcher.antMatcher(HttpMethod.POST, ApiPath.AUTH_RECOVER )).permitAll()
				.requestMatchers( AntPathRequestMatcher.antMatcher(HttpMethod.POST, ApiPath.AUTH_REGISTER) ).permitAll()
				.requestMatchers( AntPathRequestMatcher.antMatcher(HttpMethod.POST, ApiPath.AUTH_RESEND) ).permitAll()
				.requestMatchers( AntPathRequestMatcher.antMatcher(HttpMethod.POST, ApiPath.AUTH_RESET) ).permitAll()
				.requestMatchers( AntPathRequestMatcher.antMatcher(HttpMethod.POST, ApiPath.AUTH_VERIFY) ).permitAll()
				.requestMatchers( AntPathRequestMatcher.antMatcher(HttpMethod.POST, ApiPath.AUTH_LOGIN )).permitAll()
				.requestMatchers( AntPathRequestMatcher.antMatcher(HttpMethod.POST, ApiPath.AUTH_LOGOUT) ).permitAll()
				.requestMatchers( AntPathRequestMatcher.antMatcher(HttpMethod.GET, ApiPath.DASHBOARD + "/**") ).permitAll()
				.requestMatchers( AntPathRequestMatcher.antMatcher(HttpMethod.GET, ApiPath.MONITOR_STATUS) ).permitAll()
				.anyRequest().authenticated()
			)
			.addFilterAfter( new JwtFilter( jwtTokenProvider ), UsernamePasswordAuthenticationFilter.class )
			.build();
		// @formatter:on
	}

}
