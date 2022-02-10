package com.desertskyrangers.flightdeck.adapter.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebCorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings( CorsRegistry registry ) {
		registry.addMapping( ApiPath.MONITOR_STATUS ).allowedOrigins( "*" ).allowedMethods( "GET" );
		registry.addMapping( ApiPath.AUTH_RECOVER ).allowedOrigins( "*" ).allowedMethods( "POST" );
		registry.addMapping( ApiPath.AUTH_REGISTER ).allowedOrigins( "*" ).allowedMethods( "POST" );
		registry.addMapping( ApiPath.AUTH_RESEND ).allowedOrigins( "*" ).allowedMethods( "POST" );
		registry.addMapping( ApiPath.AUTH_RESET ).allowedOrigins( "*" ).allowedMethods( "POST" );
		registry.addMapping( ApiPath.AUTH_VERIFY ).allowedOrigins( "*" ).allowedMethods( "POST" );
		registry.addMapping( ApiPath.AUTH_LOGIN ).allowedOrigins( "*" ).allowedMethods( "POST" );
		// NOTE AUTH_LOGOUT should NOT be allowed from all origins
		registry.addMapping( ApiPath.API + "/**" ).allowedMethods( "GET", "POST", "PUT", "DELETE" );
	}

}
