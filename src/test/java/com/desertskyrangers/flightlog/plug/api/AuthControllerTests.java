package com.desertskyrangers.flightlog.plug.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void whenApiAuthSignup_thenSuccessResponse() throws Exception {
		Map<String, Object> request = Map.of( "username", "", "password", "", "email", "" );
		String content = new ObjectMapper().writeValueAsString( request );
		this.mockMvc.perform( post( "/api/auth/signup" ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isAccepted() );
	}

	@Test
	public void whenApiAuthSignupBadRequest_thenHandleErrorGracefully() throws Exception {
		Map<String, Object> request = Map.of();
		String content = new ObjectMapper().writeValueAsString( request );
		String message = "[\"Username required\",\"Password required\",\"EmailRequired\"]";
		this.mockMvc.perform( post( "/api/auth/signup" ).content( content ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() ).andExpect( status().reason( message ) );
	}

}
