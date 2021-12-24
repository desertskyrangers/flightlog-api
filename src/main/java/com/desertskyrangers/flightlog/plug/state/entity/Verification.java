package com.desertskyrangers.flightlog.plug.state.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table( name = "verification" )
public class Verification {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY, generator = "native" )
	private Long id;

	@Column( name = "userid" )
	private Long userId;

	private String code;

}
