package com.desertskyrangers.flightdeck.adapter.api.model;

import com.desertskyrangers.flightdeck.core.model.SmsCarrier;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Text;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.UUID;

@Data
@Accessors( chain = true )
public class ReactUser {

	private String id;

	private String username;

	private String firstName;

	private String lastName;

	private String preferredName;

	private String email;

	private boolean emailVerified;

	private String smsNumber;

	private String smsCarrier;

	private boolean smsVerified;

	public User updateFrom( User user ) {
		boolean emailChanged = Objects.equals( user.email(), this.getEmail() );
		boolean smsChanged = Objects.equals( user.smsNumber(), this.getSmsNumber() );

		user.username( this.getUsername() );
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

	public static ReactUser from( User user ) {
		ReactUser reactUser = new ReactUser();
		reactUser.setId( user.id().toString() );
		reactUser.setUsername( user.username() );
		reactUser.setFirstName( user.firstName() );
		reactUser.setLastName( user.lastName() );
		reactUser.setPreferredName( user.preferredName() );
		reactUser.setEmail( user.email() );
		reactUser.setEmailVerified( user.emailVerified() );
		reactUser.setSmsNumber( user.smsNumber() );
		reactUser.setSmsCarrier( user.smsCarrier() == null ? "" : user.smsCarrier().name().toLowerCase() );
		reactUser.setSmsVerified( user.smsVerified() );
		return reactUser;
	}

	public static User to( ReactUser user ) {
		return new User().id( UUID.fromString( user.id ) ).email( user.getEmail() );
	}
}
