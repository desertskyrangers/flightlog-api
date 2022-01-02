package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.jwt.JwtTokenProvider;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.core.model.UserToken;
import com.desertskyrangers.flightlog.core.model.Verification;
import com.desertskyrangers.flightlog.port.AuthRequesting;
import com.desertskyrangers.flightlog.port.StatePersisting;
import com.desertskyrangers.flightlog.util.Json;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@MockBean
	private AuthRequesting mockAuthRequesting;

	@Test
	public void whenApiAuthRegister_thenSuccessResponse() throws Exception {
		// given
		String username = "mockusername";
		String password = "mockpassword";
		String email = "mock@email.com";
		String content = Json.stringify( Map.of( "username", username, "password", password, "email", email ) );

		// when
		this.mockMvc.perform( post( ApiPath.AUTH_REGISTER ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isAccepted() );

		// then
		ArgumentCaptor<UserAccount> accountCaptor = ArgumentCaptor.forClass( UserAccount.class );
		ArgumentCaptor<UserToken> credentialsCaptor = ArgumentCaptor.forClass( UserToken.class );
		ArgumentCaptor<Verification> verificationCaptor = ArgumentCaptor.forClass( Verification.class );
		verify( mockAuthRequesting, times( 1 ) ).requestUserRegister( accountCaptor.capture(), credentialsCaptor.capture(), verificationCaptor.capture() );

		UserAccount account = accountCaptor.getValue();
		assertThat( account.email() ).isEqualTo( email );

		UserToken credentials = credentialsCaptor.getValue();
		assertThat( credentials.principal() ).isEqualTo( username );
		assertThat( passwordEncoder.matches( password, credentials.credential() ) ).isTrue();
	}


	@Test
	public void whenApiAuthResend_thenSuccessResponse() throws Exception {
		// given
		UUID id = UUID.randomUUID();
		String content = Json.stringify( Map.of( "id", id.toString() ) );

		// when
		this.mockMvc.perform( post( ApiPath.AUTH_RESEND ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() );

		// then
		ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass( UUID.class );
		verify( mockAuthRequesting, times( 1 ) ).requestUserVerifyResend( idCaptor.capture() );

		UUID capturedId = idCaptor.getValue();
		assertThat( capturedId ).isEqualTo( id );
	}

	@Test
	public void whenApiAuthRegisterBadRequest_thenHandleErrorGracefully() throws Exception {
		Map<String, Object> request = Map.of();
		String content = Json.stringify( request );

		Map<String, Object> result = Map.of( "messages", List.of( "Username required", "Password required", "EmailRequired" ) );
		this.mockMvc
			.perform( post( ApiPath.AUTH_REGISTER ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andExpect( content().json( Json.stringify( result ) ) );
	}

	@Test
	public void whenApiAuthVerify_thenSuccessResponse() throws Exception {
		// given
		UUID id = UUID.randomUUID();
		String code = "000000";
		String content = Json.stringify( Map.of( "id", id, "code", code ) );

		// when
		this.mockMvc.perform( post( ApiPath.AUTH_VERIFY ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() );

		// then
		ArgumentCaptor<Verification> argumentCaptor = ArgumentCaptor.forClass( Verification.class );
		verify( mockAuthRequesting, times( 1 ) ).requestUserVerify( argumentCaptor.capture() );

		Verification verification = argumentCaptor.getValue();
		assertThat( verification.id() ).isEqualTo( id );
		assertThat( verification.code() ).isEqualTo( code );
	}

	@Test
	public void whenApiAuthVerifyBadRequest_thenHandleErrorGracefully() throws Exception {
		Map<String, Object> request = Map.of();
		String content = Json.stringify( request );

		Map<String, Object> result = Map.of( "messages", List.of( "ID required", "Code required" ) );
		this.mockMvc.perform( post( ApiPath.AUTH_VERIFY ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andExpect( content().json( Json.stringify( result ) ) );
	}

	@Test
	public void whenApiAuthLogin_thenSuccessResponse() throws Exception {
		// given
		String username = "mockusername";
		String password = "mockpassword";
		String remember = "true";

		UserToken credential = new UserToken();
		credential.principal( username );
		credential.credential( passwordEncoder.encode( password ) );
		UserAccount user = new UserAccount();
		user.credentials( Set.of( credential ) );
		credential.userAccount( user );
		statePersisting.upsert( user );

		// when
		String content = Json.stringify( Map.of( "username", username, "password", password, "remember", remember ) );
		MvcResult result = this.mockMvc.perform( post( ApiPath.AUTH_LOGIN ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();

		// then
		// Parse the JSON content
		Map<String, Object> json = Json.asMap( result.getResponse().getContentAsString() );
		// Get the jwt map from the json map
		Map<?,?> jwt = (Map<?,?>)json.get("jwt");
		// Validate the generated token
		jwtTokenProvider.validateToken( jwt.get("token").toString() );
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
