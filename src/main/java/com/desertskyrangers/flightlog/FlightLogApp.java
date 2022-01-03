package com.desertskyrangers.flightlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class FlightLogApp {

	private final ApplicationContext context;

	public static void main( String[] args ) {
		ConfigurableApplicationContext context =SpringApplication.run( FlightLogApp.class, args );
		context.getBean( InitialConfig.class ).init();
	}

	public FlightLogApp( final ApplicationContext context ) {
		this.context = context;
	}

	public boolean isProduction() {
		return getActiveProfiles().contains( "prod" );
	}

	private Set<String> getActiveProfiles() {
		return new HashSet<>( Arrays.asList( context.getEnvironment().getActiveProfiles() ) );
	}

}
