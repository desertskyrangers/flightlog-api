package com.desertskyrangers.flightdeck.adapter.store.entity;

import com.desertskyrangers.flightdeck.core.model.Verification;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table( name = "verification" )
@Accessors( chain = true )
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

	public static Verification toVerification( VerificationEntity entity ) {
		Verification verification = new Verification();

		verification.id( entity.getId() );
		verification.userId( entity.getUserId() );
		verification.timestamp( entity.getTimestamp() );
		verification.code( entity.getCode() );
		verification.type( entity.getType() );

		return verification;
	}

}
