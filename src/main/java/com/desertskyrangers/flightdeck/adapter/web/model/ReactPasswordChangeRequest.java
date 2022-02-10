package com.desertskyrangers.flightdeck.adapter.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true )
public class ReactPasswordChangeRequest {

	private String id;

	private String currentPassword;

	private String password;

}
