package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.jwt.JwtToken;
import com.desertskyrangers.flightlog.adapter.api.jwt.JwtTokenProvider;
import com.desertskyrangers.flightlog.adapter.api.model.ReactProfileResponse;
import com.desertskyrangers.flightlog.adapter.api.model.ReactUserAccount;
import com.desertskyrangers.flightlog.core.UserService;
import com.desertskyrangers.flightlog.core.model.User;
import com.desertskyrangers.flightlog.core.model.UserToken;
import com.desertskyrangers.flightlog.util.Json;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith( MockitoExtension.class )
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenProvider tokenProvider;

	private User account;

	private HttpHeaders headers;

	@BeforeEach
	void setup() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if( authentication != null ) {
			String username = authentication.getName();
			String password = Objects.toString( authentication.getCredentials() );

			// Delete the exising mock user account
			userService.findByPrincipal( authentication.getName() ).ifPresent( u -> userService.remove( u ) );

			// Create mock user account
			account = new User();
			account.lastName( username );
			account.tokens( Set.of( new UserToken().principal( username ).credential( password ) ) );
			userService.upsert( account );

			String jwtToken = tokenProvider.createToken( account, authentication, false );

			headers = new HttpHeaders();
			headers.add( JwtToken.AUTHORIZATION_HEADER, JwtToken.AUTHORIZATION_TYPE + " " + jwtToken );
		}
	}

	@AfterEach
	void teardown() {
		userService.remove( account );
	}

	@Test
	void testGetProfile() throws Exception {
		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.PROFILE ).with( csrf() ).headers( headers ) ).andExpect( status().isOk() ).andReturn();

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( ReactUserAccount.from( account ) ) );
		assertThat( result.getResponse().getContentAsString() ).isEqualTo( accountJson );
	}

	@Test
	void testGetAccount() throws Exception {
		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.USER + "/" + account.id() ).with( csrf() ).headers( headers ) ).andExpect( status().isOk() ).andReturn();

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( ReactUserAccount.from( account ) ) );
		assertThat( result.getResponse().getContentAsString() ).isEqualTo( accountJson );
	}

	@Test
	void testUpdateAccount() throws Exception {
		// given
		ReactUserAccount reactAccount = ReactUserAccount.from( account );
		reactAccount.setFirstName( "Anton" );

		// when
		String content = Json.stringify( reactAccount );
		MvcResult result = this.mockMvc
			.perform( put( ApiPath.USER + "/" + account.id() ).content( content ).contentType( "application/json" ).with( csrf() ).headers( headers ) )
			.andExpect( status().isOk() )
			.andReturn();

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( reactAccount ) );
		String resultContent = result.getResponse().getContentAsString();
		assertThat( resultContent ).isEqualTo( accountJson );
		Map<String, Object> map = Json.asMap( resultContent );
		Map<String, Object> account = (Map<String, Object>)map.get( "account" );
		assertThat( account.get( "firstName" ) ).isEqualTo( "Anton" );
	}
}
