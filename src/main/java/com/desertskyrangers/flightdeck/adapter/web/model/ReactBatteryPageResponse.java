package com.desertskyrangers.flightdeck.adapter.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @deprecated In favor of {@link ReactPageResponse}
 */
@Data
@Accessors( chain = true )
@Deprecated
public class ReactBatteryPageResponse {

	private List<ReactBattery> batteries;

	private List<String> messages;

}
