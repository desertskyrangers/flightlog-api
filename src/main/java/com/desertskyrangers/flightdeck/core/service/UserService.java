package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.MemberStatus;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.core.model.UserToken;
import com.desertskyrangers.flightdeck.port.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServices {

	private final StateRetrieving stateRetrieving;

	private final StatePersisting statePersisting;

	private final PasswordEncoder passwordEncoder;

	private DashboardServices dashboardServices;

	//private PublicDashboardServices publicDashboardServices;

	public UserService( StateRetrieving stateRetrieving, StatePersisting statePersisting, PasswordEncoder passwordEncoder ) {
		this.stateRetrieving = stateRetrieving;
		this.statePersisting = statePersisting;
		this.passwordEncoder = passwordEncoder;
	}

	public void setDashboardServices( DashboardServices dashboardServices ) {
		this.dashboardServices = dashboardServices;
	}

	//	public void setPublicDashboardServices( PublicDashboardServices publicDashboardServices ) {
	//		this.publicDashboardServices = publicDashboardServices;
	//	}

	@Override
	public Set<User> find() {
		return stateRetrieving.findAllUsers();
	}

	@Override
	public Optional<User> find( UUID id ) {
		return stateRetrieving.findUser( id );
	}

	@Override
	public Optional<User> findByPrincipal( String username ) {
		return stateRetrieving.findUserTokenByPrincipal( username ).map( UserToken::user );
	}

	@Override
	public void upsert( User user ) {
		// FIXME Check for token availability, similar to AuthService.register()

		// Update security tokens if needed
		find( user.id() ).ifPresent( current -> {
			// If the username has changed, the username auth token needs to be updated also
			if( !Objects.equals( user.username(), current.username() ) ) {
				for( UserToken token : user.tokens() ) {
					if( token.principal().equals( current.username() ) ) token.principal( user.username() );
				}
			}
			// If the email address has changed, the email auth token needs to be updated also
			if( !Objects.equals( user.email(), current.email() ) ) {
				for( UserToken token : user.tokens() ) {
					if( token.principal().equals( current.email() ) ) token.principal( user.email() );
				}

				// TODO Email address needs to be verified again
			}
		} );

		statePersisting.upsert( user );
		dashboardServices.update( user );
	}

	@Override
	public void remove( User account ) {
		statePersisting.remove( account );
	}

	@Override
	public boolean isCurrentPassword( User user, String password ) {
		Optional<User> optional = find( user.id() );
		if( optional.isEmpty() ) return false;
		for( UserToken token : optional.get().tokens() ) {
			if( passwordEncoder.matches( password, token.credential() ) ) return true;
		}
		return false;
	}

	@Override
	public void updatePassword( User user, String password ) {
		find( user.id() ).ifPresent( current -> {
			String encodedPassword = passwordEncoder.encode( password );
			for( UserToken token : user.tokens() ) {
				token.credential( encodedPassword );
				statePersisting.upsert( token );
			}
		} );
	}

	@Override
	public Optional<User> findVerificationUser( UUID verificationId ) {
		return stateRetrieving.findVerification( verificationId ).flatMap( v -> stateRetrieving.findUser( v.userId() ) );
	}

	@Override
	public Set<User> findAllGroupPeers( User user ) {
		return stateRetrieving
			.findUser( user.id() )
			.stream()
			.flatMap( u -> u.groups().stream() )
			.flatMap( g -> g.users().stream() )
			.filter( u -> !Objects.equals( u, user ) )
			.collect( Collectors.toSet() );
	}

	@Override
	public Set<User> findAllAcceptedGroupPeers( User user ) {
		return stateRetrieving
			.findUser( user.id() )
			.stream()
			.flatMap( u -> u.groups().stream() )
			.flatMap( g -> g.members().stream() )
			.filter( m -> m.status() != MemberStatus.REVOKED & m.status() != MemberStatus.REQUESTED )
			.map( Member::user )
			.filter( u -> !Objects.equals( u, user ) )
			.collect( Collectors.toSet() );
	}

	@Override
	public Map<String, Object> getPreferences( User user ) {
		Map<String, Object> preferences = stateRetrieving.findPreferences( user );

		preferences.put( "username", user.username() );

		return preferences;
	}

	@Override
	public User setPreferences( User user, Map<String, Object> preferences ) {
		statePersisting.upsertPreferences( user, preferences );
		updateDashboard( user );
		return user;
	}

	private void updateDashboard( User user ) {
		// Update user dashboards
		dashboardServices.update( user );

		// Update group dashboards
		user.groups().forEach( group -> dashboardServices.update( group ) );
	}

}
