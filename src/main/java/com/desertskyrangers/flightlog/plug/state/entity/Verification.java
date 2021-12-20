package com.desertskyrangers.flightlog.plug.state.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "verification" )
public class Verification {

	@Id
	private UUID id;

	@Column( name = "userid" )
	private UUID userId;

	private String code;

}
