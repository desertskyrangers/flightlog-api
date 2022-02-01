package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.jwt.JwtTokenProvider;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.core.model.UserToken;
import com.desertskyrangers.flightdeck.core.model.Verification;
import com.desertskyrangers.flightdeck.port.AuthService;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.util.Json;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
public class AuthControllerTests extends BaseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private StatePersisting statePersisting;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@MockBean
	private AuthService mockAuthService;

	@Test
	public void whenApiAuthRecover_thenSuccessResponse() throws Exception {
		String username = "mockusernameForApiAuthRegister";
		String content = Json.stringify( Map.of( "username", username ) );

		this.mockMvc.perform( MockMvcRequestBuilders.post( ApiPath.AUTH_RECOVER ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() );
	}

	@Test
	public void whenApiAuthRecoverBadRequest_thenHandleErrorGracefully() throws Exception {
		// given
		Map<String, Object> request = Map.of();
		String content = Json.stringify( request );

		// when/then
		Map<String, Object> result = Map.of( "messages", List.of( "Username or email required" ) );
		this.mockMvc
			.perform( post( ApiPath.AUTH_RECOVER ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andExpect( content().json( Json.stringify( result ) ) );
	}

	@Test
	public void whenApiAuthReset_thenSuccessResponse() throws Exception {
		// given
		Verification verification = new Verification().userId( getMockUser().id() );
		String password = "newmockpassword";
		statePersisting.upsert( verification );

		// when/then
		String content = Json.stringify( Map.of( "id", verification.id().toString(), "password", password ) );
		this.mockMvc.perform( MockMvcRequestBuilders.post( ApiPath.AUTH_RESET ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() );
	}

	@Test
	public void whenApiAuthResetMissingId_thenHandleErrorGracefully() throws Exception {
		// given
		String content = Json.stringify( Map.of( "password", "mockpassword" ) );

		// when/then
		Map<String, Object> result = Map.of( "messages", List.of( "Reset id required" ) );
		this.mockMvc
			.perform( post( ApiPath.AUTH_RESET ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andExpect( content().json( Json.stringify( result ) ) );
	}

	@Test
	public void whenApiAuthResetMissingPassword_thenHandleErrorGracefully() throws Exception {
		String id = UUID.randomUUID().toString();
		String content = Json.stringify( Map.of( "id", id ) );

		Map<String, Object> result = Map.of( "messages", List.of( "Password required" ) );
		this.mockMvc
			.perform( post( ApiPath.AUTH_RESET ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andExpect( content().json( Json.stringify( result ) ) );
	}

	@Test
	public void whenApiAuthResetBadPassword_thenHandleErrorGracefully() throws Exception {
		String id = UUID.randomUUID().toString();
		String content = Json.stringify( Map.of( "id", id, "password", "pwd" ) );

		Map<String, Object> result = Map.of( "messages", List.of( "Invalid password" ) );
		this.mockMvc
			.perform( post( ApiPath.AUTH_RESET ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andExpect( content().json( Json.stringify( result ) ) );
	}

	@Test
	public void whenApiAuthRegister_thenSuccessResponse() throws Exception {
		// given
		String username = "mockusernameForApiAuthRegister";
		String password = "mockpassword";
		String email = username + "@example.com";
		String content = Json.stringify( Map.of( "username", username, "password", password, "email", email ) );

		User user = new User().email( email );
		statePersisting.upsert( user );

		// NOTE The user must be persisted before tokens can be persisted
		UserToken token = new UserToken().user( user ).principal( username ).credential( passwordEncoder.encode( password ) );
		user.tokens( Set.of( token ) );
		statePersisting.upsert( user );

		// when
		this.mockMvc.perform( MockMvcRequestBuilders.post( ApiPath.AUTH_REGISTER ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isAccepted() );

		// then
		ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass( String.class );
		ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass( String.class );
		ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass( String.class );
		ArgumentCaptor<UUID> verificationCaptor = ArgumentCaptor.forClass( UUID.class );
		verify( mockAuthService, times( 1 ) ).requestUserRegister( usernameCaptor.capture(), emailCaptor.capture(), passwordCaptor.capture(), verificationCaptor.capture() );

		assertThat( usernameCaptor.getValue() ).isEqualTo( username );
		assertThat( emailCaptor.getValue() ).isEqualTo( email );
		assertThat( passwordCaptor.getValue() ).isEqualTo( password );
		assertThat( verificationCaptor.getValue() ).isNotNull();
	}

	@Test
	public void whenApiAuthRegisterBadRequest_thenHandleErrorGracefully() throws Exception {
		Map<String, Object> request = Map.of();
		String content = Json.stringify( request );

		Map<String, Object> result = Map.of( "messages", List.of( "Username required", "Password required", "Email required" ) );
		this.mockMvc
			.perform( post( ApiPath.AUTH_REGISTER ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andExpect( content().json( Json.stringify( result ) ) );
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
		verify( mockAuthService, times( 1 ) ).requestUserVerifyResend( idCaptor.capture() );

		UUID capturedId = idCaptor.getValue();
		assertThat( capturedId ).isEqualTo( id );
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
		verify( mockAuthService, times( 1 ) ).requestUserVerify( argumentCaptor.capture() );

		Verification verification = argumentCaptor.getValue();
		assertThat( verification.id() ).isEqualTo( id );
		assertThat( verification.code() ).isEqualTo( code );
	}

	@Test
	public void whenApiAuthVerifyBadRequest_thenHandleErrorGracefully() throws Exception {
		Map<String, Object> request = Map.of();
		String content = Json.stringify( request );

		Map<String, Object> result = Map.of( "messages", List.of( "ID required", "Code required" ) );
		this.mockMvc
			.perform( post( ApiPath.AUTH_VERIFY ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isBadRequest() )
			.andExpect( content().json( Json.stringify( result ) ) );
	}

	@Test
	public void whenApiAuthLogin_thenSuccessResponse() throws Exception {
		// given
		String username = "mockusernameForApiAuthLogin";
		String password = "mockpassword";
		String remember = "true";

		UserToken credential = new UserToken();
		credential.principal( username );
		credential.credential( passwordEncoder.encode( password ) );
		User user = new User();
		user.tokens( Set.of( credential ) );
		credential.user( user );
		statePersisting.upsert( user );

		// when
		String content = Json.stringify( Map.of( "username", username, "password", password, "remember", remember ) );
		MvcResult result = this.mockMvc.perform( post( ApiPath.AUTH_LOGIN ).with( csrf() ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() ).andReturn();

		// then
		// Parse the JSON content
		Map<String, Object> json = Json.asMap( result.getResponse().getContentAsString() );
		// Get the jwt map from the json map
		Map<?, ?> jwt = (Map<?, ?>)json.get( "jwt" );
		// Validate the generated token
		jwtTokenProvider.validateToken( jwt.get( "token" ).toString() );
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
