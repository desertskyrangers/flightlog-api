package com.desertskyrangers.flightdeck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@Slf4j
public class FlightDeckApp {

	private final ApplicationContext context;

	public static void main( String[] args ) {
		ConfigurableApplicationContext context = SpringApplication.run( FlightDeckApp.class, args );
		context.getBean( TestDataGenerator.class ).run();
//		try {
//			Thread.sleep( 1000 );
//		} catch( InterruptedException exception ) {
//			// Ignore
//		}
		context.getBean( DataUpdate.class ).run();
	}

	public FlightDeckApp( final ApplicationContext context ) {
		this.context = context;
	}

	public boolean isProduction() {
		return getActiveProfiles().contains( "prod" );
	}

	private Set<String> getActiveProfiles() {
		return new HashSet<>( Arrays.asList( context.getEnvironment().getActiveProfiles() ) );
	}

}
