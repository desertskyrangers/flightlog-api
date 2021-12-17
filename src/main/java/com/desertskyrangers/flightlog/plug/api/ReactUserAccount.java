package com.desertskyrangers.flightlog.plug.api;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import lombok.Data;

import java.util.UUID;

@Data
class ReactUserAccount {

	private String id;

	private String username;

	private String password;

	private String email;

	public static ReactUserAccount from( UserAccount user ) {
		ReactUserAccount account = new ReactUserAccount();
		account.setId( user.id().toString() );
		account.setUsername( user.username() );
		account.setPassword( user.password() );
		account.setEmail( user.email() );
		return account;
	}

	public static UserAccount to( ReactUserAccount user ) {
		return new UserAccount().id( UUID.fromString( user.id ) ).username( user.getUsername() ).password( user.getPassword() ).email( user.getEmail() );
	}

}
