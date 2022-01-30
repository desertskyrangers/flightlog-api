package com.desertskyrangers.flightdeck.adapter.api.rest;

import com.desertskyrangers.flightdeck.adapter.api.ApiPath;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactGroup;
import com.desertskyrangers.flightdeck.adapter.api.model.ReactGroupResponse;
import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.port.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class GroupController extends BaseController {

	private final GroupService groupService;

	public GroupController( GroupService groupService ) {
		this.groupService = groupService;
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

}
