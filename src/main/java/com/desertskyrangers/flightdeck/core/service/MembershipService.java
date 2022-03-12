package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.exception.UnauthorizedException;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.HumanInterface;
import com.desertskyrangers.flightdeck.port.MembershipServices;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import com.desertskyrangers.flightdeck.util.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MembershipService implements MembershipServices {

	private static final String MEMBER_INVITE_EMAIL_TEMPLATE = "templates/member-invite.html";

	private static final String MEMBER_REQUEST_EMAIL_TEMPLATE = "templates/member-request.html";

	private final StatePersisting statePersisting;

	private final StateRetrieving stateRetrieving;

	private final HumanInterface humanInterface;

	public MembershipService( StatePersisting statePersisting, StateRetrieving stateRetrieving, HumanInterface humanInterface ) {
		this.statePersisting = statePersisting;
		this.stateRetrieving = stateRetrieving;
		this.humanInterface = humanInterface;
	}

	@Override
	public Optional<Member> find( UUID id ) {
		return stateRetrieving.findMembership( id );
	}

	@Override
	public Member upsert( User requester, Member member ) {
		// Is the requester a group owner?
		boolean isGroupOwner = stateRetrieving.findGroupOwners( member.group() ).contains( requester );

		// Is the member currently invited and accepting membership?
		Member current = stateRetrieving.findMembership( member.group(), requester ).orElse( null );
		boolean currentlyInvited = current != null && current.status() == MemberStatus.INVITED;
		boolean memberAccepted = member.status() == MemberStatus.ACCEPTED;
		boolean isAccepting = currentlyInvited && memberAccepted;

		boolean memberRequested = member.status() == MemberStatus.REQUESTED;
		boolean isRequesting = current == null && memberRequested;

		//log.info( "currentStatus=" + ( current == null ? "null" : current.status() ) + " memberStatus=" + member.status() + " isGroupOwner=" + isGroupOwner + " isAccepting=" + isAccepting );

		if( isGroupOwner || isAccepting || isRequesting ) return statePersisting.upsert( member );

		throw new UnauthorizedException( requester );
	}

	@Override
	public Member remove( User requester, Member member ) {
		return statePersisting.remove( member );
	}

	@Override
	public Set<Member> findMembershipsByUser( User user ) {
		return stateRetrieving.findMemberships( user );
	}

	public Set<Member> findMembershipsByGroup( Group group ) {
		return stateRetrieving.findMemberships( group );
	}

	public Member requestMembership( User requester, User user, Group group, MemberStatus status ) {
		if( status == MemberStatus.INVITED ) {
			Member member = upsert( requester, new Member().user( user ).group( group ).status( status ) );
			createEmailInvitations( user, group ).forEach( humanInterface::email );
			return member;
		} else if( status == MemberStatus.REQUESTED ) {
			Member member = upsert( requester, new Member().user( user ).group( group ).status( status ) );
			createEmailMembershipRequests( user, group ).forEach( humanInterface::email );
			return member;
		}

		throw new UnauthorizedException( requester );
	}

	private Set<EmailMessage> createEmailInvitations( User user, Group group ) {
		String subject = "FlightDeck Group Membership Invite";
		Map<String, Object> values = new HashMap<>();
		values.put( "subject", subject );
		values.put( "memberName", user.name() );
		values.put( "groupName", group.name() );
		String content = Template.fill( MEMBER_INVITE_EMAIL_TEMPLATE, values );
		if( content == null ) return Set.of();

		System.out.println( content );

		EmailMessage email = new EmailMessage();
		email.recipient( user.email(), user.name() );
		email.subject( subject );
		email.message( content );
		email.isHtml( true );
		return Set.of( email );
	}

	private Set<EmailMessage> createEmailMembershipRequests( User user, Group group ) {
		return stateRetrieving.findGroupOwners( group ).stream().map( o -> {
			String subject = "FlightDeck Group Membership Request";
			Map<String, Object> values = new HashMap<>();
			values.put( "subject", subject );
			values.put( "memberName", user.name() );
			values.put( "groupName", group.name() );
			String content = Template.fill( MEMBER_REQUEST_EMAIL_TEMPLATE, values );

			if( content != null ) {
				EmailMessage email = new EmailMessage();
				email.recipient( o.email(), o.name() );
				email.subject( subject );
				email.message( content );
				email.isHtml( true );
				return email;
			} else {
				return null;
			}
		} ).filter( Objects::nonNull ).collect( Collectors.toSet() );
	}

	public Member cancelMembership( User requester, Member member ) {
		remove( requester, member );
		return member;
	}

}
