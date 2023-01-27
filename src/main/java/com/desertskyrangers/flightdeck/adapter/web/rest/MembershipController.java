package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactMembership;
import com.desertskyrangers.flightdeck.adapter.web.model.ReactMembershipResponse;
import com.desertskyrangers.flightdeck.core.exception.UnauthorizedException;
import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.port.MembershipServices;
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

	private final MembershipServices memberService;

	public MembershipController( MembershipServices memberService ) {
		this.memberService = memberService;
	}

	@PutMapping( path = ApiPath.MEMBERSHIP )
	ResponseEntity<ReactMembershipResponse> updateMembership( Authentication authentication, @RequestBody Map<String, String> request ) {
		String id = request.get( "id" );
		String status = request.get( "status" );

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) || Uuid.isNotValid( id ) ) messages.add( "Invalid membership ID" );
		if( Member.Status.isNotValid( status ) ) messages.add( "Invalid member status" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactMembershipResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			User requester = getRequester( authentication );
			Optional<Member> optional = memberService.find( UUID.fromString( id ) );

			if( optional.isPresent() ) {
				Member membership = optional.get().status( Member.Status.valueOf( status.toUpperCase() ) );
				memberService.upsert( requester, membership );
				return new ResponseEntity<>( new ReactMembershipResponse().setMembership( ReactMembership.from( membership ) ), HttpStatus.OK );
			}
		} catch( UnauthorizedException exception ) {
			return new ResponseEntity<>( new ReactMembershipResponse(), HttpStatus.UNAUTHORIZED );
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
			User requester = getRequester( authentication );
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
