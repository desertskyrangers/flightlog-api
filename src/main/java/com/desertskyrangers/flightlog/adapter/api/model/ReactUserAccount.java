package com.desertskyrangers.flightlog.adapter.api.model;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( chain = true )
public class ReactUserAccount {

	private String id;

	private String firstName;

	private String lastName;

	private String email;

	private boolean emailVerified;

	private String smsNumber;

	private String smsCarrier;

	private boolean smsVerified;

	public static ReactUserAccount from( UserAccount account ) {
		ReactUserAccount reactAccount = new ReactUserAccount();
		reactAccount.setId( account.id().toString() );
		reactAccount.setFirstName( account.firstName() );
		reactAccount.setLastName( account.lastName() );
		reactAccount.setEmail( account.email() );
		reactAccount.setEmailVerified( account.emailVerified() );
		reactAccount.setSmsNumber( account.smsNumber() );
		reactAccount.setSmsCarrier( account.smsCarrier() == null ? "" : account.smsCarrier().toString() );
		reactAccount.setSmsVerified( account.smsVerified() );
		return reactAccount;
	}

	public static UserAccount to( ReactUserAccount user ) {
		return new UserAccount().id( UUID.fromString( user.id ) ).email( user.getEmail() );
	}

}
