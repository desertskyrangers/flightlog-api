package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser
@AutoConfigureMockMvc
public abstract class BaseControllerTest extends BaseTest {

	@BeforeEach
	protected void setup() {
		super.setup();
	}

}
