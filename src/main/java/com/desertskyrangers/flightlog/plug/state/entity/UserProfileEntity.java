package com.desertskyrangers.flightlog.plug.state.entity;

import com.desertskyrangers.flightlog.core.model.SmsProvider;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table( name = "userprofile" )
public class UserProfileEntity {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY, generator = "native" )
	private Long id;

	@Column( name = "preferredname" )
	private String preferredName;

	@Column( name = "smsnumber" )
	private String smsNumber;

	private SmsProvider provider;

}
