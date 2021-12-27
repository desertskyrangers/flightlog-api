package com.desertskyrangers.flightlog.adapter.state.entity;

import com.desertskyrangers.flightlog.core.model.Verification;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "verification" )
public class VerificationEntity {

	@Id
	private UUID id;

	@Column( name = "userid" )
	private UUID userId;

	private Long timestamp;

	private String code;

	private String type;

	public static VerificationEntity from( Verification verification ) {
		VerificationEntity entity = new VerificationEntity();

		entity.setId( verification.id() );
		entity.setUserId( verification.userId() );
		entity.setTimestamp( verification.timestamp() );
		entity.setCode( verification.code() );
		entity.setType( verification.type() );

		return entity;
	}
}
