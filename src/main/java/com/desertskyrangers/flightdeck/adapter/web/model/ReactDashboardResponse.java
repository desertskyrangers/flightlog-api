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
public class ReactDashboardResponse {

	private ReactDashboard dashboard;

	private List<String> messages;

}
