package com.desertskyrangers.flightlog.adapter.api.model;

import com.desertskyrangers.flightlog.core.model.SmsCarrier;
import com.desertskyrangers.flightlog.core.model.User;
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

	private String preferredName;

	private String email;

	private boolean emailVerified;

	private String smsNumber;

	private String smsCarrier;

	private boolean smsVerified;

	public User update( User user ) {
		boolean emailChanged = Objects.equals( user.email(), this.getEmail() );
		boolean smsChanged = Objects.equals( user.smsNumber(), this.getSmsNumber() );

		user.firstName( this.getFirstName() );
		user.lastName( this.getLastName() );
		user.preferredName( this.getPreferredName() );
		user.email( this.getEmail() );
		user.emailVerified( !emailChanged );
		user.smsNumber( this.getSmsNumber() );
		if( Text.isNotBlank( this.getSmsCarrier() ) ) user.smsCarrier( SmsCarrier.valueOf( this.getSmsCarrier().toUpperCase() ) );
		user.smsVerified( !smsChanged );

		return user;
	}

	public static ReactUserAccount from( User account ) {
		ReactUserAccount reactAccount = new ReactUserAccount();
		reactAccount.setId( account.id().toString() );
		reactAccount.setFirstName( account.firstName() );
		reactAccount.setLastName( account.lastName() );
		reactAccount.setPreferredName( account.preferredName() );
		reactAccount.setEmail( account.email() );
		reactAccount.setEmailVerified( account.emailVerified() );
		reactAccount.setSmsNumber( account.smsNumber() );
		reactAccount.setSmsCarrier( account.smsCarrier() == null ? "" : account.smsCarrier().name().toLowerCase() );
		reactAccount.setSmsVerified( account.smsVerified() );
		return reactAccount;
	}

	public static User to( ReactUserAccount user ) {
		return new User().id( UUID.fromString( user.id ) ).email( user.getEmail() );
	}
}
