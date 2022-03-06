package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.BaseTest;
import com.desertskyrangers.flightdeck.core.model.Battery;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.BatteryServices;
import com.desertskyrangers.flightdeck.port.FlightServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BatteryServiceTest extends BaseTest {

	@Autowired
	BatteryServices batteryServices;

	@Autowired
	FlightServices flightServices;

	@Test
	void testUpdateCycleCountWithZeroInitialCycles() {
		// given
		Battery battery = batteryServices.upsert( createTestBattery( createTestUser() ).cycles( 0 ) );
		// Because the number of flights on the battery is zero, this will get set to cycles
		assertThat( battery.initialCycles() ).isEqualTo( 0 );
		assertThat( battery.cycles() ).isEqualTo( 0 );

		// when
		batteryServices.upsert( battery.cycles( 1 ) );

		// then
		Optional<Battery> optional = batteryServices.find( battery.id() );
		assertThat( optional.isPresent() ).isTrue();
		assertThat( optional.get().initialCycles() ).isEqualTo( 1 );
		assertThat( optional.get().cycles() ).isEqualTo( 1 );
	}

	@Test
	void testUpdateCycleCountWithNonZeroInitialCyclesAndCycles() {
		// given
		Battery battery = batteryServices.upsert( createTestBattery( createTestUser() ).cycles( 1 ) );
		// Because the number of flights on the battery is zero, this will get set to cycles
		assertThat( battery.initialCycles() ).isEqualTo( 1 );
		assertThat( battery.cycles() ).isEqualTo( 1 );

		// when
		batteryServices.upsert( battery.cycles( 2 ) );

		// then
		Optional<Battery> optional = batteryServices.find( battery.id() );
		assertThat( optional.isPresent() ).isTrue();
		assertThat( optional.get().initialCycles() ).isEqualTo( 2 );
		assertThat( optional.get().cycles() ).isEqualTo( 2 );
	}

	@Test
	void testUpdateCycleCountWithAFlight() {
		// given
		User pilot = statePersisting.upsert( createTestUser() );
		Battery battery = batteryServices.upsert( createTestBattery( pilot ).cycles( 1 ) );
		// Because the number of flights on the battery is zero, this will get set to cycles
		assertThat( battery.initialCycles() ).isEqualTo( 1 );
		assertThat( battery.cycles() ).isEqualTo( 1 );

		// when
		flightServices.upsert( createTestFlight( pilot ).batteries( Set.of( battery ) ) );

		// then
		Optional<Battery> optional = batteryServices.find( battery.id() );
		assertThat( optional.isPresent() ).isTrue();
		assertThat( optional.get().initialCycles() ).isEqualTo( 1 );
		assertThat( optional.get().cycles() ).isEqualTo( 2 );
	}


	@Test
	void testUpdateCycleCountWithAFlightAndZeroCycles() {
		// NOTE It is important that initial cycles are not zero and cycles equal zero for this test

		// given
		User pilot = statePersisting.upsert( createTestUser() );
		Battery battery = statePersisting.upsert(createTestBattery( pilot ).initialCycles( 1 ).cycles( 0 ));

		// Because the number of flights on the battery is zero, this will get set to cycles
		assertThat( battery.initialCycles() ).isEqualTo( 1 );
		assertThat( battery.cycles() ).isEqualTo( 0 );

		// when
		flightServices.upsert( createTestFlight( pilot ).batteries( Set.of( battery ) ) );

		// then
		Optional<Battery> optional = batteryServices.find( battery.id() );
		assertThat( optional.isPresent() ).isTrue();
		assertThat( optional.get().initialCycles() ).isEqualTo( 1 );
		assertThat( optional.get().cycles() ).isEqualTo( 2 );
	}

}
