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
public class ReactMembershipPageResponse {

	private List<ReactMembership> memberships;

	private List<String> messages;

}
