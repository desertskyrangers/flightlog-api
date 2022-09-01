package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.GroupServices;
import com.desertskyrangers.flightdeck.port.HumanInterface;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import com.desertskyrangers.flightdeck.util.SmsCarrier;
import com.desertskyrangers.flightdeck.util.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class GroupService implements GroupServices {

	private final HumanInterface humanInterface;

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	public GroupService( HumanInterface humanInterface, StatePersisting statePersisting, StateRetrieving stateRetrieving ) {
		this.humanInterface = humanInterface;
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
	}

	@Override
	public Set<Group> findAllAvailable( User user ) {
		return stateRetrieving.findAllAvailableGroups( user );
	}

	@Override
	public Optional<Group> find( UUID id ) {
		return stateRetrieving.findGroup( id );
	}

	@Override
	public Group create( User requester, User owner, Group group ) {
		Group result = upsert( group );
		statePersisting.upsert( new Member().user( owner ).group( group ).status( MemberStatus.OWNER ) );
		return result;
	}

	@Override
	public Group upsert( Group group ) {
		return statePersisting.upsert( group );
	}

	@Override
	public Group remove( Group group ) {
		statePersisting.remove( group );
		return group;
	}

	@Override
	public Set<Group> findGroupsByUser( User user ) {
		return stateRetrieving.findGroupsByOwner( user );
	}

	@Override
	public Page<Group> findGroupsPageByUser( User user, int page, int size ) {
		return stateRetrieving.findGroupsPageByOwner( user, page, size );
	}

	@Override
	public Group callout( User caller, Group group ) {
		// Go through each member of the group and notify them of a callout
		SmsMessage sms = new SmsMessage();
		sms.content( "Flight Callout - " + caller.name() + " is going flying" );
		group.users().stream().filter( u -> Text.isNotBlank( u.smsNumber() ) ).forEach( user -> sms.recipient( user.smsNumber() ) );
		humanInterface.sms( sms );

		return group;
	}

}
