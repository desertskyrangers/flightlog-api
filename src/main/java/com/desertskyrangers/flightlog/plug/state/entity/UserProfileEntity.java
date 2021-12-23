package com.desertskyrangers.flightlog.plug.state.entity;

import com.desertskyrangers.flightlog.core.model.SmsProvider;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table( name = "userprofile" )
public class UserProfileEntity {

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO, generator = "native" )
	@GenericGenerator( name = "native", strategy = "native" )
	private Long id;

	@Column( name = "preferredname" )
	private String preferredName;

	@Column( name = "smsnumber" )
	private String smsNumber;

	private SmsProvider provider;

}
