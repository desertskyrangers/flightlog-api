package com.desertskyrangers.flightdeck.adapter.state;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TableStructureTests {

	@Autowired
	private DataSource datasource;

	@Test
	void testTables() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "aircraft" );
		expected.add( "battery" );
		expected.add( "flight" );
		expected.add( "flightbattery" );
		expected.add( "member" );
		expected.add( "org" );
		expected.add( "preferences" );
		expected.add( "user" );
		expected.add( "usertoken" );
		expected.add( "userrole" );
		expected.add( "token" );
		expected.add( "verification" );

		assertThat( getTables() ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testAircraftTable() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "name" );
		expected.add( "type" );
		expected.add( "make" );
		expected.add( "model" );
		expected.add( "status" );
		expected.add( "connector" );
		expected.add( "owner" );
		expected.add( "ownertype" );

		expected.add( "wingspan" );
		expected.add( "length" );
		expected.add( "wingarea" );
		expected.add( "weight" );

		assertThat( getColumns( "aircraft" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testBatteryTable() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "name" );
		expected.add( "make" );
		expected.add( "model" );
		expected.add( "connector" );
		expected.add( "unlistedconnector");
		expected.add( "status" );
		expected.add( "owner" );
		expected.add( "ownertype" );

		expected.add( "type" );
		expected.add( "cells" );
		expected.add( "cycles" );
		expected.add( "capacity" );
		expected.add( "dischargerating" );

		assertThat( getColumns( "battery" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testFlightTable() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "pilotid" );
		expected.add( "unlistedpilot" );
		expected.add( "observerid" );
		expected.add( "unlistedobserver" );
		expected.add( "aircraftid" );
		expected.add( "timestamp" );
		expected.add( "duration" );
		expected.add( "notes" );

		assertThat( getColumns( "flight" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testFlightBatteryTable() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "flightid" );
		expected.add( "batteryid" );

		assertThat( getColumns( "flightbattery" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testMember() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "orgid" );
		expected.add( "userid" );
		expected.add( "status" );

		assertThat( getColumns( "member" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testOrgTable() throws Exception {
		// The table is named 'org' because 'group' is a reserved word
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "type" );
		expected.add( "name" );

		assertThat( getColumns( "org" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testPreferencesTable() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "json" );

		assertThat( getColumns( "preferences" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testTokenTable() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "userid" );
		expected.add( "principal" );
		expected.add( "credential" );

		assertThat( getColumns( "token" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testUserTable() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "username" );
		expected.add( "firstname" );
		expected.add( "lastname" );
		expected.add( "preferredname" );
		expected.add( "email" );
		expected.add( "emailverified" );
		expected.add( "smsnumber" );
		expected.add( "smscarrier" );
		expected.add( "smsverified" );

		assertThat( getColumns( "user" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testVerificationTable() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "userid" );
		expected.add( "timestamp" );
		expected.add( "code" );
		expected.add( "type" );

		assertThat( getColumns( "verification" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	private Set<String> getTables() throws SQLException {
		try( Connection connection = datasource.getConnection() ) {
			ResultSet result = connection.createStatement().executeQuery( "show tables" );

			Set<String> tables = new HashSet<>();
			while( result.next() ) tables.add( result.getString( 1 ).toLowerCase() );

			return tables;
		}
	}

	private Set<String> getColumns( String table ) throws SQLException {
		try( Connection connection = datasource.getConnection() ) {
			ResultSet result = connection.createStatement().executeQuery( "show columns from " + table );

			Set<String> columns = new HashSet<>();
			while( result.next() ) columns.add( result.getString( 1 ).toLowerCase() );

			return columns;
		}
	}

}
