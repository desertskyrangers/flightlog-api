package com.desertskyrangers.flightlog.adapter.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

		@Override
		public void addCorsMappings( CorsRegistry registry ) {
			registry.addMapping( ApiPath.AUTH_LOGIN ).allowedOrigins( "*" ).allowedMethods( "POST" );
			registry.addMapping( ApiPath.AUTH_REGISTER ).allowedOrigins( "*" ).allowedMethods( "POST" );
			registry.addMapping( ApiPath.AUTH_VERIFY ).allowedOrigins( "*" ).allowedMethods( "POST" );
			registry.addMapping( ApiPath.MONITOR_STATUS ).allowedOrigins( "*" ).allowedMethods( "GET" );
			registry.addMapping( "/**" ).allowedMethods( "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH" );
		}

}
