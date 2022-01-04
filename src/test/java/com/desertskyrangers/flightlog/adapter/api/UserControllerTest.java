package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactProfileResponse;
import com.desertskyrangers.flightlog.adapter.api.model.ReactUserAccount;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.util.Json;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith( MockitoExtension.class )
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetApiProfile() throws Exception {
		// given

		// when
		MvcResult result = this.mockMvc.perform( get( ApiPath.PROFILE ).with( csrf() ) ).andExpect( status().isOk() ).andReturn();

		// then
		String accountJson = Json.stringify( new ReactProfileResponse().setAccount( new ReactUserAccount() ) );
		assertThat( result.getResponse().getStatus() ).isEqualTo( 200 );
		assertThat( result.getResponse().getContentAsString() ).isEqualTo( accountJson );
	}

}
