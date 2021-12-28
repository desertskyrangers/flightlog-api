package com.desertskyrangers.flightlog.adapter.api.model;

import com.desertskyrangers.flightlog.core.model.UserCredentials;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
public class ReactUserSignup {

	private String id;

	private String username;

	private String password;

	private String email;

	public static ReactUserSignup from( UserCredentials user ) {
		ReactUserSignup signup = new ReactUserSignup();
		signup.setId( user.id().toString() );
		signup.setUsername( user.username() );
		signup.setPassword( user.password() );
		return signup;
	}

	public static UserCredentials to( ReactUserSignup user ) {
		return new UserCredentials().id( UUID.fromString( user.id ) ).username( user.getUsername() ).password( user.getPassword() );
	}

}
