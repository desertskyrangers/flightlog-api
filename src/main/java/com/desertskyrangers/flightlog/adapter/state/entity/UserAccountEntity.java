package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import lombok.Data;

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

	@OneToMany
	private Set<UserCredentialEntity> credentials;

	public static UserAccountEntity from( UserAccount account ) {
		UserAccountEntity entity = new UserAccountEntity();

		entity.setId( account.id() );
		entity.setPreferredName( account.preferredName() );
		entity.setSmsNumber( account.smsNumber() );
		entity.setSmsNumber( account.smsProvider().name() );

		entity.getCredentials().addAll( account.credentials().stream().map( UserCredentialEntity::from ).collect( Collectors.toSet()) );

		return entity;
	}

}
