package com.desertskyrangers.flightlog.core;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserToken;
import com.desertskyrangers.flightlog.port.StateRetrieving;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppUserDetailsServiceTest {

	@Mock
	StateRetrieving stateRetrieving;

	@InjectMocks
	private AppUserDetailsService appUserDetailsService;

	@Test
	void testLoadUserByUsername() {
		// given
		String username = "testuser";
		String password = "password";
		UserToken credential = new UserToken().principal( username ).credential( password );

		UserAccount user = new UserAccount();
		credential.userAccount( user );
		user.credentials( Set.of( credential ) );

		when( stateRetrieving.findUserTokenByPrincipal( username ) ).thenReturn( Optional.of( credential ) );

		// when
		UserDetails details = appUserDetailsService.loadUserByUsername( username );

		// then
		assertThat( details ).isInstanceOf( AppPrincipal.class );
		AppPrincipal principal = (AppPrincipal)details;
		assertThat( principal.getUsername() ).isEqualTo( username );
		assertThat( principal.getPassword() ).isEqualTo( password );
		assertThat( principal.getAuthorities() ).containsExactlyInAnyOrder( AppRole.USER );
	}

}
