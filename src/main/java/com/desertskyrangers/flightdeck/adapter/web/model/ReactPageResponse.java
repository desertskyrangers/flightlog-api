package com.desertskyrangers.flightdeck.adapter.web.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Accessors( chain = true )
public class ReactPageResponse<T> {

	private Page<? extends T> page;

	private List<String> messages;

	private ReactPageResponse() {}

	public static <S> ReactPageResponse<S> of( Page<S> page ) {
		return new ReactPageResponse<S>().setPage( page );
	}

	public static ReactPageResponse<?> messages( List<String> messages ) {
		return new ReactPageResponse<>().setMessages( messages );
	}

}
