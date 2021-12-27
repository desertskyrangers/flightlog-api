package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.core.model.UserAccount;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
		ArgumentCaptor<UserAccount> argumentCaptor = ArgumentCaptor.forClass( UserAccount.class );
		verify( authRequesting, times( 1 ) ).requestUserAccountSignup( argumentCaptor.capture() );

		UserAccount account = argumentCaptor.getValue();
		assertThat( account.username() ).isEqualTo( username );
		assertThat( account.password() ).isEqualTo( password );
		assertThat( account.email() ).isEqualTo( email );
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
