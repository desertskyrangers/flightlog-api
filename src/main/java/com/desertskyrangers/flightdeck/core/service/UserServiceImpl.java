package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.core.model.UserToken;
import com.desertskyrangers.flightdeck.port.StatePersisting;
import com.desertskyrangers.flightdeck.port.StateRetrieving;
import com.desertskyrangers.flightdeck.port.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

	private final StateRetrieving stateRetrieving;

	private final StatePersisting statePersisting;

	public UserServiceImpl( StateRetrieving stateRetrieving, StatePersisting statePersisting ) {
		this.stateRetrieving = stateRetrieving;
		this.statePersisting = statePersisting;
	}

	@Override
	public List<User> find() {
		return stateRetrieving.findAllUserAccounts();
	}

	@Override
	public Optional<User> find( UUID id ) {
		return stateRetrieving.findUserAccount( id );
	}

	@Override
	public Optional<User> findByPrincipal( String username ) {
		return stateRetrieving.findUserTokenByPrincipal( username ).map( UserToken::user );
	}

	@Override
	public void upsert( User account ) {
		// If the email address has changed, the email auth token needs to be updated also
		find( account.id() ).ifPresent( current -> {
			if( !Objects.equals( account.email(), current.email() ) ) {
				for( UserToken token : account.tokens() ) {
					if( token.principal().equals( current.email() ) ) token.principal( account.email() );
				}
			}
		} );

		statePersisting.upsert( account );
	}

	@Override
	public void remove( User account ) {
		statePersisting.remove( account );
	}

	@Override
	public Optional<User> findVerificationUser( UUID verificationId ) {
		return stateRetrieving.findVerification( verificationId ).flatMap( v -> stateRetrieving.findUserAccount( v.userId() ) );
	}

}
