package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.UserAccount;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

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

	public static UserAccountEntity from( UserAccount profile ) {
		UserAccountEntity entity = new UserAccountEntity();

		entity.setId( profile.id() );
		entity.setPreferredName( profile.preferredName() );
		entity.setSmsNumber( profile.smsNumber() );
		entity.setSmsNumber( profile.smsProvider().name() );

		return entity;
	}

}
