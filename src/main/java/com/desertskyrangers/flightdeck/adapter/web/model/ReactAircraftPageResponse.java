package com.desertskyrangers.flightdeck.adapter.web.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @deprecated In favor of {@link ReactPageResponse}
 */
@Data
@Accessors( chain = true )
@Deprecated
public class ReactAircraftPageResponse {

	private Page<ReactAircraft> aircraft;

	private List<String> messages;

}
