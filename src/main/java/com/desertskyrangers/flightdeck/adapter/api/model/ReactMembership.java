package com.desertskyrangers.flightdeck.adapter.api.model;

import com.desertskyrangers.flightdeck.core.model.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ReactMembership {

	private String id;

	private ReactUser user;

	private ReactGroup group;

	private String status;

	public static ReactMembership from( Member member ) {
		ReactMembership reactMembership = new ReactMembership();

		reactMembership.setId( member.id().toString() );
		reactMembership.setUser( ReactUser.from( member.user() ) );
		reactMembership.setGroup( ReactGroup.from( member.group() ) );
		reactMembership.setStatus( member.status().name().toLowerCase() );

		return reactMembership;
	}

}
