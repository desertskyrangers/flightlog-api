package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredential;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppPrincipalServiceTest {

	@Mock
	StateRetrieving stateRetrieving;

	@InjectMocks
	private AppPrincipalService appPrincipalService;

	@Test
	void testLoadUserByUsername() {
		// given
		String username = "testuser";
		String password = "password";
		UserCredential credential = new UserCredential().username( username ).password( password );

		UserAccount user = new UserAccount();
		credential.userAccount( user );
		user.credentials( Set.of( credential ) );

		when( stateRetrieving.findUserCredentialByUsername( username ) ).thenReturn( Optional.of( credential ) );

		// when
		UserDetails details = appPrincipalService.loadUserByUsername( username );

		// then
		assertThat( details ).isInstanceOf( AppPrincipal.class );
		AppPrincipal principal = (AppPrincipal)details;
		assertThat( principal.getUsername() ).isEqualTo( username );
		assertThat( principal.getPassword() ).isEqualTo( password );
		assertThat( principal.getAuthorities() ).containsExactlyInAnyOrder( AppRole.USER );
	}

}
