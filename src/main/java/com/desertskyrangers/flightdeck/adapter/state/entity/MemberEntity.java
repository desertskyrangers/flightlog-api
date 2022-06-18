package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.MemberStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Map;
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

	/**
	 * This method is specifically built to avoid a stack overflow when converting
	 * a membership record from the {@link GroupEntity#toGroup(GroupEntity)} method.
	 *
	 * @param entity
	 * @param groups
	 * @param members
	 * @return
	 */
	static Member toMemberFromGroup( MemberEntity entity, Map<UUID, Group> groups, Map<UUID, Member> members ) {
		Member member = members.get( entity.getId() );
		if( member != null ) return member;

		member = toMemberSkipGroup( entity );
		members.put( entity.getId(), member );
		//member.groups( entity.getGroups().stream().map( e -> GroupEntity.toGroupFromUser( e, members, groups ) ).collect( Collectors.toSet() ) );
		return member;
	}

	private static Member toMemberSkipGroup( MemberEntity entity ) {
		Member member = new Member();

		member.id( entity.getId() );
		member.user( UserEntity.toUser( entity.getUser() ) );
		//member.group( GroupEntity.toGroup( entity.getGroup() ) );
		member.status( MemberStatus.valueOf( entity.getStatus().toUpperCase() ) );

		return member;
	}

}
