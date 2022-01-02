package com.desertskyrangers.flightlog.adapter.api.model;

import com.desertskyrangers.flightlog.core.model.UserToken;
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

	public static ReactRegisterRequest from( UserToken user ) {
		ReactRegisterRequest signup = new ReactRegisterRequest();
		signup.setId( user.id().toString() );
		signup.setUsername( user.principal() );
		signup.setPassword( user.credential() );
		return signup;
	}

	public static UserToken to( ReactRegisterRequest user ) {
		return new UserToken().id( UUID.fromString( user.id ) ).principal( user.getUsername() ).credential( user.getPassword() );
	}

}
