package com.desertskyrangers.flightlog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FlightLogAppTests {

	@Value( "${spring.application.version:unknown}" )
	String version;

	@Autowired
	ApplicationContext context;

	@Autowired
	FlightLogApp app;

	@Test
	void contextLoads() {
		assertThat( context ).isNotNull();
		assertThat( context.getEnvironment().getProperty( "server.port" ) ).isEqualTo( "8050" );
		assertThat( context.getEnvironment().getProperty( "spring.application.version" ) ).isEqualTo( version );
	}

	@Test
	void appLoads() {
		assertThat( app ).isNotNull();
		assertThat( app.isProduction() ).isFalse();
	}

}
