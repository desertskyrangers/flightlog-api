package com.desertskyrangers.flightdeck.adapter.store.entity;

import com.desertskyrangers.flightdeck.core.model.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
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
	private String type;

	@Column( nullable = false )
	private String name;

	@Column( name = "dashboardid" )
	private UUID dashboardId;

	@ManyToMany( fetch = FetchType.EAGER )
	@JoinTable( name = "member", joinColumns = @JoinColumn( name = "orgid" ), inverseJoinColumns = @JoinColumn( name = "userid" ) )
	@EqualsAndHashCode.Exclude
	@ToString.Exclude()
	private Set<UserEntity> users = new HashSet<>();

	@OneToMany( mappedBy = "group", fetch = FetchType.EAGER )
	@EqualsAndHashCode.Exclude
	@ToString.Exclude()
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
		entity.setDashboardId( group.dashboardId() );

		return entity;
	}

	public static Group toGroup( GroupEntity entity ) {
		Group group = toGroupShallow( entity );

		final Map<UUID, Group> groups = new HashMap<>();
		final Map<UUID, Member> members = new HashMap<>();
		final Map<UUID, Location> locations = new HashMap<>();
		final Map<UUID, User> users = new HashMap<>();

		groups.put( entity.getId(), group );

		group.members( entity.getMemberships().stream().map( e -> MemberEntity.toMemberFromRelated( e, members, groups, locations, users ) ).collect( Collectors.toSet() ) );
		group.users( entity.getUsers().stream().map( e -> UserEntity.toUserFromRelated( e, users, groups, locations, members ) ).collect( Collectors.toSet() ) );

		return group;
	}

	static Group toGroupFromRelated( GroupEntity entity, Map<UUID, Group> groups, Map<UUID, Member> members, Map<UUID, Location> locations, Map<UUID, User> users ) {
		// If the group already exists, just return it
		Group group = groups.get( entity.getId() );
		if( group != null ) return group;

		// Create the shallow version of the group and put it in the groups map
		group = toGroupShallow( entity );
		groups.put( entity.getId(), group );

		// Link the group to related entities
		group.members( entity.getMemberships().stream().map( e -> MemberEntity.toMemberFromRelated( e, members, groups, locations, users ) ).collect( Collectors.toSet() ) );
		group.users( entity.getUsers().stream().map( e -> UserEntity.toUserFromRelated( e, users, groups, locations, members ) ).collect( Collectors.toSet() ) );
		return group;
	}

	private static Group toGroupShallow( GroupEntity entity ) {
		Group group = new Group();

		group.id( entity.getId() );
		group.type( Group.Type.valueOf( entity.getType().toUpperCase() ) );
		group.name( entity.getName() );
		group.dashboardId( entity.getDashboardId() );

		return group;
	}

}
