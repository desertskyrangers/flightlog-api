package com.desertskyrangers.flightdeck.adapter.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true )
public class ReactVerifyRequest {

	private String id;

	private String code;

}
