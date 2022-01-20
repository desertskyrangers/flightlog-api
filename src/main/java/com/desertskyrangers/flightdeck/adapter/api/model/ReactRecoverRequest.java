package com.desertskyrangers.flightdeck.adapter.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true )
public class ReactRecoverRequest {

	private String username;

}
