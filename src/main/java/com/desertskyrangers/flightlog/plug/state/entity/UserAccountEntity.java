package com.desertskyrangers.flightlog.plug.state.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table( name = "useraccount" )
public class UserAccountEntity {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY, generator = "native" )
	private Integer id;

	private String username;

	private String password;

	private String email;

}
