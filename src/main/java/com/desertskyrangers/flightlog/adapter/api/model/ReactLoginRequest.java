package com.desertskyrangers.flightlog.adapter.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true )
public class ReactLoginRequest {

	private String username;

	private String password;

	private boolean remember;

}
