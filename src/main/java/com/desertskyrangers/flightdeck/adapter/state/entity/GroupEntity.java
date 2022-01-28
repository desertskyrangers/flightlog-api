package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.GroupType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
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
		GroupEntity entity = new GroupEntity();

		entity.setId( group.id() );
		entity.setType( group.type().name().toLowerCase() );
		entity.setName( group.name() );
		if( group.owner() != null ) entity.setOwner( UserEntity.from( group.owner() ) );
		entity.setMembers( group.members().stream().map( UserEntity::from ).collect( Collectors.toSet() ) );

		return entity;
	}

	public static Group toGroup( GroupEntity entity ) {
		Group group = new Group();

		group.id( entity.getId() );
		group.type( GroupType.valueOf( entity.getType().toUpperCase() ) );
		group.name( entity.getName() );
		group.owner( UserEntity.toUserShallow( entity.getOwner() ) );
		group.members( entity.getMembers().stream().map( UserEntity::toUserShallow ).collect( Collectors.toSet() ) );

		return group;
	}

}
