package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactGroup;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactGroupResponse;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactOption;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.GroupService;
import com.desertskyrangers.flightdeck.port.MemberService;
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

	private final MemberService memberService;

	public GroupController( GroupService groupService, MemberService memberService ) {
		this.groupService = groupService;
		this.memberService = memberService;
	}

	@GetMapping( path = ApiPath.GROUPS_AVAILABLE )
	ResponseEntity<List<ReactOption>> getAvailableGroups( Authentication authentication ) {
		User user = findUser( authentication );
		List<Group> objects = new ArrayList<>( groupService.findAllAvailable( user ) );
		Collections.sort( objects );
		return new ResponseEntity<>( objects.stream().map( c -> new ReactOption( c.id().toString(), c.name() ) ).toList(), HttpStatus.OK );
	}

	@GetMapping( path = ApiPath.GROUP + "/{id}" )
	ResponseEntity<ReactGroupResponse> getGroup( @PathVariable UUID id ) {
		log.info( "Get group" );
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
		return updateGroup( authentication, request );
	}

	@PutMapping( path = ApiPath.GROUP )
	ResponseEntity<ReactGroupResponse> updateGroup( Authentication authentication, @RequestBody ReactGroup request ) {
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
			User user = findUser( authentication );
			Group group = groupService.upsert( ReactGroup.toGroup( request ) );
			memberService.upsert( new Member().user( user ).group( group ).status( MemberStatus.OWNER ) );
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
