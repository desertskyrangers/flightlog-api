package com.desertskyrangers.flightlog.adapter.api.model;

import com.desertskyrangers.flightlog.core.model.SmsCarrier;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.util.Text;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;
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

	public UserAccount update( UserAccount user ) {
		boolean emailChanged = Objects.equals( user.email(), this.getEmail() );
		boolean smsChanged = Objects.equals( user.smsNumber(), this.getSmsNumber() );

		if( getFirstName() != null ) user.firstName( this.getFirstName() );
		if( this.getLastName() != null ) user.lastName( this.getLastName() );
		if( this.getEmail() != null ) user.email( this.getEmail() );
		user.emailVerified( !emailChanged );
		if( this.getSmsNumber() != null ) user.smsNumber( this.getSmsNumber() );
		if( Text.isNotBlank( this.getSmsCarrier() ) ) user.smsCarrier( SmsCarrier.valueOf( this.getSmsCarrier().toUpperCase() ) );
		user.smsVerified( !smsChanged );

		return user;
	}

	public static ReactUserAccount from( UserAccount account ) {
		ReactUserAccount reactAccount = new ReactUserAccount();
		reactAccount.setId( account.id().toString() );
		reactAccount.setFirstName( account.firstName() );
		reactAccount.setLastName( account.lastName() );
		reactAccount.setEmail( account.email() );
		reactAccount.setEmailVerified( account.emailVerified() );
		reactAccount.setSmsNumber( account.smsNumber() );
		reactAccount.setSmsCarrier( account.smsCarrier() == null ? "" : account.smsCarrier().name() );
		reactAccount.setSmsVerified( account.smsVerified() );
		return reactAccount;
	}

	public static UserAccount to( ReactUserAccount user ) {
		return new UserAccount().id( UUID.fromString( user.id ) ).email( user.getEmail() );
	}
}
