package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserCredentials;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.util.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith( MockitoExtension.class )
public class AuthControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthRequesting authRequesting;

	@Test
	public void whenApiAuthSignup_thenSuccessResponse() throws Exception {
		// given
		String username = "mockusername";
		String password = "mockpassword";
		String email = "mock@email.com";
		String content = new ObjectMapper().writeValueAsString( Map.of( "username", username, "password", password, "email", email ) );

		// when
		this.mockMvc.perform( post( ApiPath.AUTH_SIGNUP ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isAccepted() );

		// then
		ArgumentCaptor<UserAccount> accountCaptor = ArgumentCaptor.forClass( UserAccount.class );
		ArgumentCaptor<UserCredentials> credentialsCaptor = ArgumentCaptor.forClass( UserCredentials.class );
		verify( authRequesting, times( 1 ) ).requestUserAccountSignup( accountCaptor.capture(), credentialsCaptor.capture() );

		UserAccount account = accountCaptor.getValue();
		assertThat( account.email() ).isEqualTo( email );

		UserCredentials credentials = credentialsCaptor.getValue();
		assertThat( credentials.username() ).isEqualTo( username );
		assertThat( credentials.password() ).isEqualTo( password );
	}

	@Test
	public void whenApiAuthSignupBadRequest_thenHandleErrorGracefully() throws Exception {
		Map<String, Object> request = Map.of();
		String content = Json.stringify( request );

		Map<String, Object> result = Map.of( "messages", List.of( "Username required", "Password required", "EmailRequired" ) );
		this.mockMvc
			.perform( post( ApiPath.AUTH_SIGNUP ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andExpect( content().json( Json.stringify( result ) ) );
	}

	@Test
	public void whenApiAuthVerify_thenSuccessResponse() throws Exception {
		// given
		UUID id = UUID.randomUUID();
		String code = "543210";
		String url = ApiPath.AUTH_VERIFY + "?id=" + id + "&code=" + code;

		// when
		this.mockMvc.perform( get( url ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() );

		// then
		ArgumentCaptor<Verification> argumentCaptor = ArgumentCaptor.forClass( Verification.class );
		verify( authRequesting, times( 1 ) ).requestUserVerify( argumentCaptor.capture() );

		Verification verification = argumentCaptor.getValue();
		assertThat( verification.id() ).isEqualTo( id );
		assertThat( verification.code() ).isEqualTo( code );
	}

	@Test
	public void whenApiAuthVerifyBadRequest_thenHandleErrorGracefully() throws Exception {
		String url = ApiPath.AUTH_VERIFY + "?id=&code=";

		Map<String, Object> result = Map.of( "messages", List.of( "ID required", "Code required" ) );
		this.mockMvc.perform( get( url ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andExpect( content().json( Json.stringify( result ) ) );
	}

	@Test
	public void whenApiAuthLoginBadRequest_thenHandleErrorGracefully() throws Exception {
		Map<String, Object> request = Map.of();
		String content = Json.stringify( request );

		Map<String, Object> result = Map.of( "messages", List.of( "Username required", "Password required" ) );
		this.mockMvc
			.perform( post( ApiPath.AUTH_LOGIN ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andExpect( content().json( Json.stringify( result ) ) );
	}

}
