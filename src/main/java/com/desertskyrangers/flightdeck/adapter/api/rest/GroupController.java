package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.*;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.GroupService;
import com.desertskyrangers.flightdeck.port.MembershipService;
import com.desertskyrangers.flightdeck.port.UserService;
import com.desertskyrangers.flightdeck.util.Text;
import com.desertskyrangers.flightdeck.util.Uuid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class GroupController extends BaseController {

	private final GroupService groupService;

	private final MembershipService membershipService;

	private final UserService userService;

	public GroupController( GroupService groupService, MembershipService membershipService, UserService userService ) {
		this.groupService = groupService;
		this.membershipService = membershipService;
		this.userService = userService;
	}

	@PostMapping( path = ApiPath.GROUP_INVITE )
	ResponseEntity<ReactMembershipPageResponse> inviteMember( Authentication authentication, @RequestBody Map<String, String> request ) {
		String id = request.get( "id" );
		String invitee = request.get( "invitee" );

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Text.isNotBlank( id ) && Uuid.isNotValid( id ) ) messages.add( "Invalid group id" );
		if( Text.isBlank( invitee ) ) messages.add( "Invitee required" );
		Optional<Group> optionalGroup = groupService.find( UUID.fromString( id ) );
		if( optionalGroup.isEmpty() ) messages.add( "Group not found" );
		Optional<User> optionalInvitee = userService.findByPrincipal( invitee );
		if( optionalInvitee.isEmpty() ) messages.add( "Invitee not found: " + invitee );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactMembershipPageResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			User requester = getRequester( authentication );
			membershipService.requestMembership( requester, optionalInvitee.get(), optionalGroup.get(), MemberStatus.INVITED );

			Set<Member> memberships = membershipService.findMembershipsByGroup( optionalGroup.get() );
			List<Member> objects = new ArrayList<>( memberships );
			Collections.sort( objects );
			return new ResponseEntity<>( new ReactMembershipPageResponse().setMemberships( objects.stream().map( ReactMembership::from ).toList() ), HttpStatus.ACCEPTED );
		} catch( Exception exception ) {
			log.warn( "Error inviting group member", exception );
			return new ResponseEntity<>( new ReactMembershipPageResponse().setMessages( List.of( "Error inviting group member" ) ), HttpStatus.BAD_REQUEST );
		}
	}

	@GetMapping( path = ApiPath.GROUP_AVAILABLE )
	ResponseEntity<List<ReactOption>> getAvailableGroups( Authentication authentication ) {
		User user = getRequester( authentication );
		List<Group> objects = new ArrayList<>( groupService.findAllAvailable( user ) );
		Collections.sort( objects );
		return new ResponseEntity<>( objects.stream().map( c -> new ReactOption( c.id().toString(), c.name() ) ).toList(), HttpStatus.OK );
	}

	@GetMapping( path = ApiPath.GROUP + "/{id}/membership" )
	ResponseEntity<List<ReactMembership>> getGroupMembership( @PathVariable String id ) {
		Optional<Group> optional = groupService.find( UUID.fromString( id ) );

		if( optional.isPresent() ) {
			Set<Member> memberships = membershipService.findMembershipsByGroup( optional.get() );
			List<Member> objects = new ArrayList<>( memberships );
			Collections.sort( objects );
			return new ResponseEntity<>( objects.stream().map( ReactMembership::from ).toList(), HttpStatus.OK );
		} else {
			return new ResponseEntity<>( List.of(), HttpStatus.BAD_REQUEST );
		}
	}

	@GetMapping( path = ApiPath.GROUP + "/{id}" )
	ResponseEntity<ReactGroupResponse> getGroup( @PathVariable UUID id ) {
		List<String> messages = new ArrayList<>();
		try {
			Optional<Group> optional = groupService.find( id );
			if( optional.isPresent() ) {
				return new ResponseEntity<>( new ReactGroupResponse().setGroup( ReactGroup.from( optional.get() ) ), HttpStatus.OK );
			} else {
				messages.add( "Group id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error getting group", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactGroupResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PostMapping( path = ApiPath.GROUP )
	ResponseEntity<ReactGroupResponse> newGroup( Authentication authentication, @RequestBody ReactGroup request ) {
		// Replace 'new' with random id
		request.setId( UUID.randomUUID().toString() );
		return updateGroup( authentication, request, true );
	}

	@PutMapping( path = ApiPath.GROUP )
	ResponseEntity<ReactGroupResponse> updateGroup( Authentication authentication, @RequestBody ReactGroup request, boolean isNew ) {
		String id = request.getId();
		String type = request.getType();
		String name = request.getName();

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Text.isNotBlank( id ) && Uuid.isNotValid( id ) ) messages.add( "Invalid group id" );
		if( GroupType.isNotValid( type ) ) messages.add( "Type required" );
		if( Text.isBlank( name ) ) messages.add( "Name required" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactGroupResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			User requester = getRequester( authentication );
			log.warn( "Is new group=" + isNew );
			Group group = ReactGroup.toGroup( request );
			if( isNew ) {
				groupService.create( requester, requester, group );
			} else {
				groupService.upsert( group );
			}
			return new ResponseEntity<>( new ReactGroupResponse().setGroup( request ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error updating group", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactGroupResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@DeleteMapping( path = ApiPath.GROUP )
	ResponseEntity<ReactGroupResponse> deleteGroup( @RequestBody Map<String, Object> group ) {
		UUID id = UUID.fromString( String.valueOf( group.get( "id" ) ) );
		log.info( "Delete group" );
		List<String> messages = new ArrayList<>();
		try {
			Optional<Group> optional = groupService.find( id );
			if( optional.isPresent() ) {
				Group deletedGroup = optional.get();
				groupService.remove( deletedGroup );
				return new ResponseEntity<>( new ReactGroupResponse().setGroup( ReactGroup.from( deletedGroup ) ), HttpStatus.OK );
			} else {
				messages.add( "Group id not found: " + id );
			}
		} catch( Exception exception ) {
			log.error( "Error removing group", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactGroupResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

}
