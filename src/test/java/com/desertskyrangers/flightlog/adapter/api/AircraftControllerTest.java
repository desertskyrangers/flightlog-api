package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactAircraft;
import com.desertskyrangers.flightlog.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
public class AircraftControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetAircraftPage() throws Exception {
//		ReactAircraft aftyn = new ReactAircraft().setName( "AFTYN" );
//		ReactAircraft bianca = new ReactAircraft().setName( "BIANCA" );
//		List<ReactAircraft> response = List.of( aftyn, bianca );


		MvcResult result = this.mockMvc.perform( get( ApiPath.USER_AIRCRAFT + "/0" ) ).andExpect( status().isOk() ).andReturn();

		Map<String, Object> map = Json.asMap( result.getResponse().getContentAsString() );
		List<?> aircraftList = (List<?>)map.get( "aircraft" );
		Map<?, ?> messagesMap = (Map<?, ?>)map.get( "messages" );

		assertThat( aircraftList.size() ).isEqualTo( 2 );
		assertThat( messagesMap ).isNull();

		Map<?, ?> aircraft0 = (Map<?, ?>)aircraftList.get( 0 );
		Map<?, ?> aircraft1 = (Map<?, ?>)aircraftList.get( 1 );
		assertThat( aircraft0.get( "name" ) ).isEqualTo( "AFTYN" );
		assertThat( aircraft1.get( "name" ) ).isEqualTo( "BIANCA" );
	}

}
