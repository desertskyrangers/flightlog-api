package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.GroupType;
import com.desertskyrangers.flightdeck.core.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Table( name = "org" )
public class GroupEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	@Column( nullable = false )
	String type;

	@Column( nullable = false )
	String name;

	// This is a user account id
	@ManyToOne( optional = false, fetch = FetchType.EAGER )
	@JoinColumn( name = "ownerid", nullable = false, updatable = false, columnDefinition = "BINARY(16)" )
	private UserEntity owner;

	@ManyToMany( fetch = FetchType.EAGER )
	@JoinTable( name = "orguser", joinColumns = @JoinColumn( name = "orgid" ), inverseJoinColumns = @JoinColumn( name = "userid" ) )
	@EqualsAndHashCode.Exclude
	private Set<UserEntity> members;

	public static GroupEntity from( Group group ) {
		GroupEntity entity = fromShallow( group );

		final Map<UUID, GroupEntity> groups = new HashMap<>();
		final Map<UUID, UserEntity> users = new HashMap<>();
		if( group.owner() != null ) entity.setOwner( UserEntity.fromUserFromGroup( group.owner(), users, groups ) );
		entity.setMembers( group.members().stream().map( u -> UserEntity.fromUserFromGroup( u, users, groups ) ).collect( Collectors.toSet() ) );

		return entity;
	}

	static GroupEntity fromGroupFromUser( Group group, Map<UUID, GroupEntity> groups, Map<UUID, UserEntity> users ) {
		GroupEntity entity = groups.get(group.id() );
		if( entity != null ) return entity;

		entity = fromShallow(group);
		groups.put(group.id(), entity);
		if( group.owner() != null ) entity.setOwner( UserEntity.fromUserFromGroup( group.owner(), users, groups ) );
		entity.setMembers( group.members().stream().map( u -> UserEntity.fromUserFromGroup( u, users, groups ) ).collect( Collectors.toSet() ) );

		return entity;
	}

	private static GroupEntity fromShallow( Group group ) {
		GroupEntity entity = new GroupEntity();

		entity.setId( group.id() );
		entity.setType( group.type().name().toLowerCase() );
		entity.setName( group.name() );

		return entity;
	}

	public static Group toGroup( GroupEntity entity ) {
		Group group = toGroupShallow( entity );

		final Map<UUID, Group> groups = new HashMap<>();
		final Map<UUID, User> users = new HashMap<>();
		groups.put( entity.getId(), group );

		group.owner( UserEntity.toUserFromGroup( entity.getOwner(), groups, users ) );
		group.members( entity.getMembers().stream().map( e -> UserEntity.toUserFromGroup( e, groups, users ) ).collect( Collectors.toSet() ) );

		return group;
	}

	static Group toGroupFromUser( GroupEntity entity, Map<UUID, User> users, Map<UUID, Group> groups ) {
		Group group = groups.get( entity.getId() );
		if( group != null ) return group;

		group = toGroupShallow( entity );
		groups.put( entity.getId(), group );
		group.owner( UserEntity.toUserFromGroup( entity.getOwner(), groups, users ) );
		group.members( entity.getMembers().stream().map( e -> UserEntity.toUserFromGroup( e, groups, users ) ).collect( Collectors.toSet() ) );
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
