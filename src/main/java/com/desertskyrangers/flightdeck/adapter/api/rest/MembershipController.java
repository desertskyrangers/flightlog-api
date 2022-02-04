package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactMembership;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactMembershipResponse;
import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.MemberStatus;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.MembershipService;
import com.desertskyrangers.flightdeck.util.Text;
import com.desertskyrangers.flightdeck.util.Uuid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
public class MembershipController extends BaseController {

	private final MembershipService memberService;

	public MembershipController( MembershipService memberService ) {
		this.memberService = memberService;
	}

	@PutMapping( path = ApiPath.MEMBERSHIP )
	ResponseEntity<ReactMembershipResponse> updateMembership( Authentication authentication, @RequestBody Map<String, String> request ) {
		String id = request.get( "id" );
		String status = request.get( "status" );

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) || Uuid.isNotValid( id ) ) messages.add( "Invalid membership ID" );
		if( MemberStatus.isNotValid( status ) ) messages.add( "Invalid member status" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactMembershipResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			User requester = findUser( authentication );
			Optional<Member> optional = memberService.find( UUID.fromString( id ) );

			if( optional.isPresent() ) {
				Member membership = optional.get();
				memberService.upsert( requester, membership.status( MemberStatus.valueOf( status.toUpperCase() ) ) );
				return new ResponseEntity<>( new ReactMembershipResponse().setMembership( ReactMembership.from( membership ) ), HttpStatus.OK );
			}
		} catch( Exception exception ) {
			log.error( "Error updating flight", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactMembershipResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@DeleteMapping( path = ApiPath.MEMBERSHIP )
	ResponseEntity<ReactMembershipResponse> removeMembership( Authentication authentication, @RequestBody Map<String, String> request ) {
		String id = request.get( "id" );

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) || Uuid.isNotValid( id ) ) messages.add( "Invalid membership ID" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactMembershipResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			User requester = findUser( authentication );
			Optional<Member> optional = memberService.find( UUID.fromString( id ) );

			if( optional.isPresent() ) {
				Member membership = optional.get();
				memberService.remove( requester, membership );
				return new ResponseEntity<>( new ReactMembershipResponse().setMembership( ReactMembership.from( membership ) ), HttpStatus.OK );
			}
		} catch( Exception exception ) {
			log.error( "Error updating flight", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactMembershipResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

}
