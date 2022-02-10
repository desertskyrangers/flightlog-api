package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtToken;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( chain = true )
public class ReactResetResponse {

	private String id;

	private JwtToken jwt;

	private List<String> messages;

}
