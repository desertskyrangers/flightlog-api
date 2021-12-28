package com.desertskyrangers.flightlog.adapter.api.model;

import com.desertskyrangers.flightlog.core.model.UserCredentials;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
public class ReactSignupRequest {

	private String id;

	private String username;

	private String password;

	private String email;

	public static ReactSignupRequest from( UserCredentials user ) {
		ReactSignupRequest signup = new ReactSignupRequest();
		signup.setId( user.id().toString() );
		signup.setUsername( user.username() );
		signup.setPassword( user.password() );
		return signup;
	}

	public static UserCredentials to( ReactSignupRequest user ) {
		return new UserCredentials().id( UUID.fromString( user.id ) ).username( user.getUsername() ).password( user.getPassword() );
	}

}
