package com.desertskyrangers.flightlog.plug.state.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table( name = "verification" )
public class Verification {

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO, generator = "native" )
	@GenericGenerator( name = "native", strategy = "native" )
	private Long id;

	@Column( name = "userid" )
	private Long userId;

	private String code;

}
