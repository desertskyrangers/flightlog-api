package com.desertskyrangers.flightlog.adapter.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

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
			.csrf().ignoringAntMatchers( ApiPath.AUTH_VERIFY ).and()
			.authorizeRequests()
				.mvcMatchers( HttpMethod.GET, "/api/auth/csrf" ).permitAll()
				.mvcMatchers( HttpMethod.POST, ApiPath.AUTH_LOGIN ).permitAll()
				.mvcMatchers( HttpMethod.POST, ApiPath.AUTH_REGISTER ).permitAll()
				.mvcMatchers( HttpMethod.GET, ApiPath.AUTH_VERIFY ).permitAll()
				.mvcMatchers( HttpMethod.GET, ApiPath.MONITOR_STATUS ).permitAll()
				.anyRequest().authenticated()

			// FIXME Eventually remove basic auth
			.and().httpBasic();
		// @formatter:on
	}

}
