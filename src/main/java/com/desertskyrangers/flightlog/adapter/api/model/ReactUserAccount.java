package com.desertskyrangers.flightlog.adapter.api.model;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
public class ReactUserAccount {

	private String id;

	private String email;

	public static ReactUserAccount from( UserAccount account ) {
		ReactUserAccount reactAccount = new ReactUserAccount();
		reactAccount.setId( account.id().toString() );
		reactAccount.setEmail( account.email() );
		return reactAccount;
	}

	public static UserAccount to( ReactUserAccount user ) {
		return new UserAccount().id( UUID.fromString( user.id ) ).email( user.getEmail() );
	}

}
