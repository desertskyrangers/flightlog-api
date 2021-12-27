package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.SmsProvider;
import lombok.Data;

import javax.persistence.*;
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

	private SmsProvider provider;

}
