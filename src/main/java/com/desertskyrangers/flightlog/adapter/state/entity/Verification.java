package com.desertskyrangers.flightlog.adapter.state.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table( name = "verification" )
public class Verification {

	@Id
	private UUID id;

	@Column( name = "userid" )
	private Long userId;

	private String code;

}
