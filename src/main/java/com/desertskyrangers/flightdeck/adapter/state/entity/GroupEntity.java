package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.GroupType;
import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.core.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table( name = "org" )
@Accessors( chain = true )
public class GroupEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( nullable = false )
	String type;

	@Column( nullable = false )
	String name;

	@ManyToMany( fetch = FetchType.EAGER )
	@JoinTable( name = "member", joinColumns = @JoinColumn( name = "orgid" ), inverseJoinColumns = @JoinColumn( name = "userid" ) )
	@EqualsAndHashCode.Exclude
	private Set<UserEntity> users = new HashSet<>();

	@OneToMany( mappedBy = "group", fetch = FetchType.EAGER )
	@EqualsAndHashCode.Exclude
	private Set<MemberEntity> memberships = new HashSet<>();

	public static GroupEntity from( Group group ) {
		GroupEntity entity = fromGroupShallow( group );

		final Map<UUID, GroupEntity> groups = new HashMap<>();
		final Map<UUID, UserEntity> users = new HashMap<>();
		entity.setUsers( group.users().stream().map( u -> UserEntity.fromUserFromGroup( u, users, groups ) ).collect( Collectors.toSet() ) );

		return entity;
	}

	static GroupEntity fromGroupFromUser( Group group, Map<UUID, GroupEntity> groups, Map<UUID, UserEntity> users ) {
		GroupEntity entity = groups.get( group.id() );
		if( entity != null ) return entity;

		entity = fromGroupShallow( group );
		groups.put( group.id(), entity );
		entity.setUsers( group.users().stream().map( u -> UserEntity.fromUserFromGroup( u, users, groups ) ).collect( Collectors.toSet() ) );

		return entity;
	}

	private static GroupEntity fromGroupShallow( Group group ) {
		GroupEntity entity = new GroupEntity();

		entity.setId( group.id() );
		entity.setType( group.type().name().toLowerCase() );
		entity.setName( group.name() );

		return entity;
	}

	public static Group toGroup( GroupEntity entity ) {
		Group group = toGroupShallow( entity );

		final Map<UUID, Group> groups = new HashMap<>();
		final Map<UUID, Member> members = new HashMap<>();
		final Map<UUID, User> users = new HashMap<>();
		groups.put( entity.getId(), group );
		group.members( entity.getMemberships().stream().map( e -> MemberEntity.toMemberFromGroup( e, groups, members ) ).collect( Collectors.toSet() ) );
		group.users( entity.getUsers().stream().map( e -> UserEntity.toUserFromGroup( e, groups, users ) ).collect( Collectors.toSet() ) );

		return group;
	}

	static Group toGroupFromUser( GroupEntity entity, Map<UUID, User> users, Map<UUID, Group> groups ) {
		Group group = groups.get( entity.getId() );
		if( group != null ) return group;

		group = toGroupShallow( entity );
		groups.put( entity.getId(), group );
		group.users( entity.getUsers().stream().map( e -> UserEntity.toUserFromGroup( e, groups, users ) ).collect( Collectors.toSet() ) );
		return group;
	}

	private static Group toGroupShallow( GroupEntity entity ) {
		Group group = new Group();

		group.id( entity.getId() );
		group.type( GroupType.valueOf( entity.getType().toUpperCase() ) );
		group.name( entity.getName() );

		return group;
	}

}
