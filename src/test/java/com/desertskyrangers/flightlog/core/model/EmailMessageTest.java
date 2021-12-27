package com.desertskyrangers.flightlog.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailMessageTest {

	@Test
	void setHtmlFlagAutomaticallyWithHtmlTaggedMessage() {
		EmailMessage message = new EmailMessage();
		assertThat( message.isHtml()).isFalse();

		message.message("<html><head></head><body></body></html>");
		assertThat( message.isHtml()).isTrue();
	}

	@Test
	void setHtmlFlagAutomaticallyWithHtmlDoctypeMessage() {
		EmailMessage message = new EmailMessage();
		assertThat( message.isHtml()).isFalse();

		message.message("<!DOCTYPE html><html><head></head><body></body></html>");
		assertThat( message.isHtml()).isTrue();
	}

}
