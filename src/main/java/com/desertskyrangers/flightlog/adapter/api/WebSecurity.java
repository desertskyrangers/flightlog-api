package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.jwt.JwtConfigurer;
import com.desertskyrangers.flightlog.adapter.api.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final JwtTokenProvider jwtTokenProvider;

	public WebSecurity( JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	// Expose the AuthenticationManager bean
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure( AuthenticationManagerBuilder authentication ) throws Exception {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		authentication
			.inMemoryAuthentication()
			.withUser( "user" )
			.password( encoder.encode( "password" ) )
			.roles( "USER" )
			.and()
			.withUser( "admin" )
			.password( encoder.encode( "admin" ) )
			.roles( "USER", "ADMIN" );
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
			.and().apply( new JwtConfigurer( jwtTokenProvider ) );
		// @formatter:on
	}

}
