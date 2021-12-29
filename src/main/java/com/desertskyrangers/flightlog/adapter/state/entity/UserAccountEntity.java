package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.SmsProvider;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Table( name = "useraccount" )
public class UserAccountEntity {

	@Id
	private UUID id;

	@Column( name = "firstname" )
	private String firstName;

	@Column( name = "lastname" )
	private String lastName;

	@Column( name = "preferredname" )
	private String preferredName;

	private String email;

	@Column( name = "emailverified" )
	private boolean emailVerified;

	@Column( name = "smsnumber" )
	private String smsNumber;

	@Column( name = "smsprovider" )
	private String provider;

	@Column( name = "smsverified" )
	private boolean smsVerified;

	@EqualsAndHashCode.Exclude
	@OneToMany( cascade = CascadeType.ALL )
	private Set<UserCredentialEntity> credentials;

	public static UserAccountEntity from( UserAccount account ) {
		UserAccountEntity entity = new UserAccountEntity();

		entity.setId( account.id() );
		entity.setPreferredName( account.preferredName() );
		entity.setSmsNumber( account.smsNumber() );
		if( account.smsProvider() != null ) entity.setSmsNumber( account.smsProvider().name().toLowerCase() );

		entity.setCredentials( account.credentials().stream().map( UserCredentialEntity::from ).peek( c -> c.setUserAccount( entity ) ).collect( Collectors.toSet() ) );

		return entity;
	}

	public static UserAccount toUserAccount( UserAccountEntity entity ) {
		UserAccount account = new UserAccount();

		account.id( entity.getId() );
		account.firstName( entity.getFirstName() );
		account.lastName( entity.getLastName() );
		account.preferredName( entity.getPreferredName() );
		account.email( entity.getEmail() );
		account.emailVerified( entity.isEmailVerified() );
		account.smsNumber( entity.getSmsNumber() );
		if( entity.getSmsNumber() != null ) account.smsProvider( SmsProvider.valueOf( entity.getSmsNumber().toUpperCase() ) );
		account.smsVerified( entity.isSmsVerified() );

		// FIXME have to do this with a new session or not be lazy
		//account.credentials( entity.getCredentials().stream().map( c -> UserCredentialEntity.toUserCredential( account, c ) ).collect( Collectors.toSet() ) );

		return account;
	}

}
