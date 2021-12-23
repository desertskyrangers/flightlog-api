package com.desertskyrangers.flightlog.plug.state.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table( name = "useraccount" )
public class UserAccountEntity {

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO, generator = "native" )
	@GenericGenerator( name = "native", strategy = "native" )
	private Long id;

	private String username;

	private String password;

	private String email;

}
