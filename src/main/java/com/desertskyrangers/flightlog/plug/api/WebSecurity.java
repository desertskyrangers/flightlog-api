package com.desertskyrangers.flightlog.plug.api;

import org.springframework.context.annotation.Configuration;
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
		// FIXME Had to turn off CSRF for login to work
		// Need to figure out how to mix csrf, cors and public pages
		http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();

//		http.authorizeRequests()
//			.antMatchers( HttpMethod.POST, "/api/auth/login" ).permitAll()
//			.antMatchers( HttpMethod.POST, "/api/auth/signup" ).permitAll()
//			.antMatchers( HttpMethod.GET, "/api/monitor/status" ).permitAll()
//			.anyRequest().authenticated().and().httpBasic();
	}

}
