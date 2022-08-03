package com.desertskyrangers.flightdeck.adapter.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @deprecated In favor of {@link ReactResponse}
 */
@Data
@Accessors( chain = true )
@Deprecated
public class ReactProfileResponse {

	public ReactUser account;

	public List<String> messages;

}
