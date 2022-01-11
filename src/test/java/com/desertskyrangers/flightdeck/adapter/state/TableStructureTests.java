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
		expected.add( "user" );
		expected.add( "token" );
		expected.add( "verification" );
		expected.add( "usertoken" );
		expected.add( "userrole" );
		expected.add( "aircraft" );
		expected.add( "org" );

		assertThat( getTables() ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testToken() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "userid" );
		expected.add( "principal" );
		expected.add( "credential" );

		assertThat( getColumns( "token" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testUser() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
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
	void testVerification() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "userid" );
		expected.add( "timestamp" );
		expected.add( "code" );
		expected.add( "type" );

		assertThat( getColumns( "verification" ) ).containsAll( expected );
	}

	@Test
	void testAircraft() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "name" );
		expected.add( "type" );
		expected.add( "make" );
		expected.add( "model" );
		expected.add( "status" );
		expected.add( "owner" );
		expected.add( "ownertype" );

		assertThat( getColumns( "aircraft" ) ).containsAll( expected );
	}


	@Test
	void testOrg() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "name" );
		expected.add( "ownerid" );
		//expected.add( "type" );

		assertThat( getColumns( "org" ) ).containsAll( expected );
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
