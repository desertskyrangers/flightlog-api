package com.desertskyrangers.flightlog.plug.state.entity;

import com.desertskyrangers.flightlog.core.model.SmsProvider;
import lombok.Data;

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

	private String preferredName;

	private String smsNumber;

	private SmsProvider provider;

}
