package com.desertskyrangers.flightlog.adapter.state;

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
		expected.add( "useraccount" );
		expected.add( "usercredential" );
		expected.add( "verification" );

		assertThat( getTables() ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testUserCredential() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "username" );
		expected.add( "password" );

		assertThat( getColumns( "usercredential" ) ).containsExactlyInAnyOrderElementsOf( expected );
	}

	@Test
	void testUserAccount() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );
		expected.add( "preferredname" );
		expected.add( "email" );
		expected.add( "emailverified" );
		expected.add( "smsnumber" );
		expected.add( "smsprovider" );
		expected.add( "smsverified" );

		assertThat( getColumns( "useraccount" ) ).containsExactlyInAnyOrderElementsOf( expected );
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
