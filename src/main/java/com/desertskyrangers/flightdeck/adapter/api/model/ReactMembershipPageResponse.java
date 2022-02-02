package com.desertskyrangers.flightdeck.adapter.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( chain = true )
public class ReactMembershipPageResponse {

	private List<ReactMembership> memberships;

	private List<String> messages;

}
