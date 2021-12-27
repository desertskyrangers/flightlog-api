package com.desertskyrangers.flightlog.adapter.state.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table( name = "authentication" )
public class AuthenticationEntity {

	@Id
	private UUID id;

	@Column( name = "userid" )
	private UUID userId;

	String type;

//	@ManyToMany
//	Map<String,String> credentials;

}
