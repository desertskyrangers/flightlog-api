package com.desertskyrangers.flightlog.plug.state;

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
public class StateRepoTests {

	@Autowired
	private DataSource datasource;

	@Test
	void testUserAccount() throws Exception {
		Set<String> expected = new HashSet<>();
		expected.add( "id" );

		assertThat( getColumns( "useraccount" )).containsAll( expected );
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
