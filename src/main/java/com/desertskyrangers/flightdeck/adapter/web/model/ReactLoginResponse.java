package com.desertskyrangers.flightdeck.adapter.web.model;

import com.desertskyrangers.flightdeck.adapter.web.jwt.JwtToken;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @deprecated In favor of {@link ReactResponse}
 */
@Data
@Accessors( chain = true )
@Deprecated
public class ReactLoginResponse {

	private JwtToken jwt;

	private List<String> messages;

}
