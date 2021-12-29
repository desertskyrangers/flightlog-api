package com.desertskyrangers.flightlog.adapter.api.model;

import com.desertskyrangers.flightlog.core.model.UserCredential;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
public class ReactRegisterRequest {

	private String id;

	private String username;

	private String password;

	private String email;

	public static ReactRegisterRequest from( UserCredential user ) {
		ReactRegisterRequest signup = new ReactRegisterRequest();
		signup.setId( user.id().toString() );
		signup.setUsername( user.username() );
		signup.setPassword( user.password() );
		return signup;
	}

	public static UserCredential to( ReactRegisterRequest user ) {
		return new UserCredential().id( UUID.fromString( user.id ) ).username( user.getUsername() ).password( user.getPassword() );
	}

}
