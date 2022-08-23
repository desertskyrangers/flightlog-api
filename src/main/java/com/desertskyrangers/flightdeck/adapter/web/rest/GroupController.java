package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.model.*;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.GroupServices;
import com.desertskyrangers.flightdeck.port.MembershipServices;
import com.desertskyrangers.flightdeck.port.ProjectionServices;
import com.desertskyrangers.flightdeck.port.UserServices;
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

	private final GroupServices groupServices;

	private final MembershipServices membershipServices;

	private final UserServices userServices;

	private final ProjectionServices projectionServices;

	public GroupController( GroupServices groupServices, MembershipServices membershipServices, UserServices userServices, ProjectionServices projectionServices ) {
		this.groupServices = groupServices;
		this.membershipServices = membershipServices;
		this.userServices = userServices;
		this.projectionServices = projectionServices;
	}

	@GetMapping( path = ApiPath.GROUP + "/{id}/dashboard" )
	ResponseEntity<?> dashboard( Authentication authentication, @PathVariable String id ) {
		User user = getRequester( authentication );

		List<String> messages = new ArrayList<>();
		// Get and verify the group id
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Uuid.isNotValid( id ) ) messages.add( "Invalid ID" );
		// Get and verify the group
		Optional<Group> optionalGroup = groupServices.find( UUID.fromString( id ) );
		if( optionalGroup.isEmpty() ) messages.add( "Group not found" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

		// Verify group membership
		Group group = optionalGroup.get();
		if( !group.users().contains( user ) ) messages.add( "User not a member of group" );
		// Get and verify the dashboard
		Optional<String> projection = projectionServices.findProjection( group.dashboardId() );
		if( projection.isEmpty() ) messages.add( "Error retrieving group dashboard" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

		try {
			return new ResponseEntity<>( ReactResponse.wrapProjection( projection.get() ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.warn( "Error retrieving group dashboard", exception );
			return new ResponseEntity<>( ReactResponse.messages( List.of( "Error retrieving group dashboard" ) ), HttpStatus.BAD_REQUEST );
		}
	}

	@PostMapping( path = ApiPath.GROUP_INVITE )
	ResponseEntity<ReactMembershipPageResponse> inviteMember( Authentication authentication, @RequestBody Map<String, String> request ) {
		String id = request.get( "id" );
		String invitee = request.get( "invitee" );

		List<String> messages = new ArrayList<>();
		if( Text.isBlank( id ) ) messages.add( "ID required" );
		if( Text.isNotBlank( id ) && Uuid.isNotValid( id ) ) messages.add( "Invalid id" );
		if( Text.isBlank( invitee ) ) messages.add( "Invitee required" );
		Optional<Group> optionalGroup = groupServices.find( UUID.fromString( id ) );
		if( optionalGroup.isEmpty() ) messages.add( "Group not found" );
		Optional<User> optionalInvitee = userServices.findByPrincipal( invitee );
		if( optionalInvitee.isEmpty() ) messages.add( "Invitee not found: " + invitee );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactMembershipPageResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			User requester = getRequester( authentication );
			membershipServices.requestMembership( requester, optionalInvitee.get(), optionalGroup.get(), MemberStatus.INVITED );

			Set<Member> memberships = membershipServices.findMembershipsByGroup( optionalGroup.get() );
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
		List<Group> objects = new ArrayList<>( groupServices.findAllAvailable( user ) );
		Collections.sort( objects );
		return new ResponseEntity<>( objects.stream().map( c -> new ReactOption( c.id().toString(), c.name() ) ).toList(), HttpStatus.OK );
	}

	@GetMapping( path = ApiPath.GROUP + "/{id}/membership" )
	ResponseEntity<List<ReactMembership>> getGroupMembership( @PathVariable String id ) {
		Optional<Group> optional = groupServices.find( UUID.fromString( id ) );

		if( optional.isPresent() ) {
			Set<Member> memberships = membershipServices.findMembershipsByGroup( optional.get() );
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
			Optional<Group> optional = groupServices.find( id );
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
				groupServices.create( requester, requester, group );
			} else {
				groupServices.upsert( group );
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
			Optional<Group> optional = groupServices.find( id );
			if( optional.isPresent() ) {
				Group deletedGroup = optional.get();
				groupServices.remove( deletedGroup );
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
