package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.MemberStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table( name = "member" )
@Accessors( chain = true )
public class MemberEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@ManyToOne( optional = false, fetch = FetchType.EAGER )
	@JoinColumn( name = "userid", nullable = false, updatable = false, columnDefinition = "BINARY(16)" )
	private UserEntity user;

	@ManyToOne( optional = false, fetch = FetchType.EAGER )
	@JoinColumn( name = "orgid", nullable = false, updatable = false, columnDefinition = "BINARY(16)" )
	private GroupEntity group;

	private String status;

	public static MemberEntity from( Member member ) {
		MemberEntity entity = new MemberEntity();
		entity.setId( member.id() );
		entity.setUser( UserEntity.from( member.user() ) );
		entity.setGroup( GroupEntity.from( member.group() ) );
		entity.setStatus( member.status().name().toLowerCase() );
		return entity;
	}

	public static Member toMember( MemberEntity entity ) {
		Member member = new Member();
		member.id( entity.getId() );
		member.user( UserEntity.toUser( entity.getUser() ) );
		member.group( GroupEntity.toGroup( entity.getGroup() ) );
		member.status( MemberStatus.valueOf( entity.getStatus().toUpperCase() ) );
		return member;
	}

}
