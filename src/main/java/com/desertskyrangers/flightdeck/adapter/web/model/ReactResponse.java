package com.desertskyrangers.flightdeck.adapter.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( chain = true )
public class ReactResponse<T> {

	private T data;

	private List<String> messages;

	private ReactResponse() {}

	public static <S> ReactResponse<S> of( S data ) {
		return new ReactResponse<S>().setData( data );
	}

	public static <S> ReactResponse<S> messages( List<String> messages ) {
		return new ReactResponse<S>().setMessages( messages );
	}

	public static String wrapProjection( String projection ) {
		return "{\"data\":" + projection + "}";
	}

}
