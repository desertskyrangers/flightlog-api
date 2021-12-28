package com.desertskyrangers.flightlog.adapter.state.entity;

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
	@OneToMany(cascade = CascadeType.ALL)
	private Set<UserCredentialEntity> credentials;

	public static UserAccountEntity from( UserAccount account ) {
		UserAccountEntity entity = new UserAccountEntity();

		entity.setId( account.id() );
		entity.setPreferredName( account.preferredName() );
		entity.setSmsNumber( account.smsNumber() );
		if( account.smsProvider() != null ) entity.setSmsNumber( account.smsProvider().name() );

		entity.setCredentials( account.credentials().stream().map( UserCredentialEntity::from ).peek( c -> c.setUserAccount( entity ) ).collect( Collectors.toSet() ) );

		return entity;
	}

}
