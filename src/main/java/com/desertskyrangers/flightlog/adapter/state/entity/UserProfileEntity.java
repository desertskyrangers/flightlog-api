package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.UserProfile;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "userprofile" )
public class UserProfileEntity {

	@Id
	private UUID id;

	@Column( name = "preferredname" )
	private String preferredName;

	@Column( name = "smsnumber" )
	private String smsNumber;

	@Column( name = "smsprovider" )
	private String provider;

	public static UserProfileEntity from( UserProfile profile ) {
		UserProfileEntity entity = new UserProfileEntity();

		entity.setId( profile.user().id() );
		entity.setPreferredName( profile.preferredName() );
		entity.setSmsNumber( profile.smsNumber() );
		entity.setSmsNumber( profile.smsProvider().name() );

		return entity;
	}

}
