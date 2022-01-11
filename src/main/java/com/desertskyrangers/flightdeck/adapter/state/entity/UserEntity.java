package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.SmsCarrier;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Text;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Table( name = "user" )
public class UserEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( name = "firstname" )
	private String firstName;

	@Column( name = "lastname" )
	private String lastName;

	@Column( name = "preferredname" )
	private String preferredName;

	private String email;

	@Column( name = "emailverified" )
	private Boolean emailVerified;

	@Column( name = "smsnumber" )
	private String smsNumber;

	@Column( name = "smscarrier" )
	private String smsCarrier;

	@Column( name = "smsverified" )
	private Boolean smsVerified;

	@OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	@CollectionTable( name = "usertoken", joinColumns = @JoinColumn( name = "userid" ) )
	@EqualsAndHashCode.Exclude
	private Set<TokenEntity> tokens = new HashSet<>();

	@ElementCollection
	@Column( name = "roles", nullable = false )
	@CollectionTable( name = "userrole", joinColumns = @JoinColumn( name = "userid" ) )
	@Fetch( FetchMode.JOIN )
	@EqualsAndHashCode.Exclude
	private Set<String> roles = new HashSet<>();

	public static UserEntity from( User user ) {
		return fromUserAccount( user, true );
	}

	public static User toUserAccount( UserEntity entity ) {
		User user = new User();

		user.id( entity.getId() );
		user.firstName( entity.getFirstName() );
		user.lastName( entity.getLastName() );
		user.preferredName( entity.getPreferredName() );
		user.email( entity.getEmail() );
		user.emailVerified( entity.getEmailVerified() != null && entity.getEmailVerified() );
		user.smsNumber( entity.getSmsNumber() );
		if( Text.isNotBlank( entity.getSmsCarrier() ) ) user.smsCarrier( SmsCarrier.valueOf( entity.getSmsCarrier().toUpperCase() ) );
		user.smsVerified( entity.getSmsVerified() != null && entity.getSmsVerified() );
		user.tokens( entity.getTokens().stream().map( c -> TokenEntity.toUserCredential( c ).user( user ) ).collect( Collectors.toSet() ) );
		user.roles( entity.getRoles() );

		return user;
	}

	static UserEntity fromWithoutCredential( User user ) {
		return fromUserAccount( user, false );
	}

	private static UserEntity fromUserAccount( User user, boolean includeTokens ) {
		UserEntity entity = new UserEntity();

		entity.setId( user.id() );
		entity.setFirstName( user.firstName() );
		entity.setLastName( user.lastName() );
		entity.setPreferredName( user.preferredName() );
		entity.setEmail( user.email() );
		entity.setEmailVerified( user.emailVerified() );
		entity.setSmsNumber( user.smsNumber() );
		if( user.smsCarrier() != null ) entity.setSmsCarrier( user.smsCarrier().name().toLowerCase() );
		entity.setSmsVerified( user.smsVerified() );
		if( includeTokens ) entity.setTokens( user.tokens().stream().map( TokenEntity::from ).peek( c -> c.setUserAccount( entity ) ).collect( Collectors.toSet() ) );
		entity.setRoles( user.roles() );

		return entity;
	}

}
