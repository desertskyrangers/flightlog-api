package com.desertskyrangers.flightdeck.adapter.state.entity;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.SmsCarrier;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Text;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table( name = "user" )
public class UserEntity {

	@Id
	@Column( columnDefinition = "BINARY(16)" )
	private UUID id;

	private String username;

	@Column( name = "firstname" )
	private String firstName;

	@Column( name = "lastname" )
	private String lastName;

	@Column( name = "preferredname" )
	private String preferredName;

	private String email;

	@Column( name = "emailverified" )
	private Boolean emailVerified;

	@Column( name = "smsnumber" )
	private String smsNumber;

	@Column( name = "smscarrier" )
	private String smsCarrier;

	@Column( name = "smsverified" )
	private Boolean smsVerified;

	@OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	@CollectionTable( name = "usertoken", joinColumns = @JoinColumn( name = "userid" ) )
	@EqualsAndHashCode.Exclude
	private Set<TokenEntity> tokens = new HashSet<>();

	@ElementCollection
	@Column( name = "roles", nullable = false )
	@CollectionTable( name = "userrole", joinColumns = @JoinColumn( name = "userid" ) )
	@Fetch( FetchMode.JOIN )
	@EqualsAndHashCode.Exclude
	private Set<String> roles = new HashSet<>();

	@ManyToMany( fetch = FetchType.EAGER )
	@JoinTable( name = "member", joinColumns = @JoinColumn( name = "userid" ), inverseJoinColumns = @JoinColumn( name = "orgid" ) )
	@EqualsAndHashCode.Exclude
	private Set<GroupEntity> groups = new HashSet<>();

	@OneToMany( mappedBy = "user", fetch = FetchType.EAGER )
	@EqualsAndHashCode.Exclude
	private Set<MemberEntity> memberships = new HashSet<>();

	public static UserEntity from( User user ) {
		UserEntity entity = fromUserShallow( user );

		Map<UUID, UserEntity> users = new HashMap<>();
		Map<UUID, TokenEntity> tokens = new HashMap<>();
		Map<UUID, GroupEntity> groups = new HashMap<>();
		entity.setTokens( user.tokens().stream().map( t -> TokenEntity.fromTokenFromUser( t, tokens, users ) ).collect( Collectors.toSet() ) );
		entity.setGroups( user.groups().stream().map( g -> GroupEntity.fromGroupFromUser( g, groups, users ) ).collect( Collectors.toSet() ) );

		return entity;
	}

	static UserEntity fromUserFromToken( User user, Map<UUID, UserEntity> users, Map<UUID, TokenEntity> tokens ) {
		UserEntity entity = users.get( user.id() );
		if( entity != null ) return entity;

		entity = fromUserShallow( user );
		users.put( user.id(), entity );
		entity.setTokens( user.tokens().stream().map( t -> TokenEntity.fromTokenFromUser( t, tokens, users ) ).collect( Collectors.toSet() ) );

		return entity;
	}

	static UserEntity fromUserFromGroup( User user, Map<UUID, UserEntity> users, Map<UUID, GroupEntity> groups ) {
		UserEntity entity = users.get( user.id() );
		if( entity != null ) return entity;

		entity = fromUserShallow( user );
		users.put( user.id(), entity );
		entity.setGroups( user.groups().stream().map( g -> GroupEntity.fromGroupFromUser( g, groups, users ) ).collect( Collectors.toSet() ) );

		return entity;
	}

	public static User toUser( UserEntity entity ) {
		User user = toUserShallow( entity );

		final Map<UUID, User> users = new HashMap<>();
		final Map<UUID, Group> groups = new HashMap<>();
		users.put( entity.getId(), user );

		user.groups( entity.getGroups().stream().map( e -> GroupEntity.toGroupFromUser( e, users, groups ) ).collect( Collectors.toSet() ) );

		return user;
	}

	/**
	 * This method is specifically built to avoid a stack overflow when converting
	 * a user record from the {@link GroupEntity#toGroup(GroupEntity)} method.
	 *
	 * @param entity
	 * @param groups
	 * @param users
	 * @return
	 */
	static User toUserFromGroup( UserEntity entity, Map<UUID, Group> groups, Map<UUID, User> users ) {
		User user = users.get( entity.getId() );
		if( user != null ) return user;

		user = toUserShallow( entity );
		users.put( entity.getId(), user );
		user.groups( entity.getGroups().stream().map( e -> GroupEntity.toGroupFromUser( e, users, groups ) ).collect( Collectors.toSet() ) );
		return user;
	}

	private static UserEntity fromUserShallow( User user ) {
		UserEntity entity = new UserEntity();

		entity.setId( user.id() );
		entity.setUsername( user.username() );
		entity.setFirstName( user.firstName() );
		entity.setLastName( user.lastName() );
		entity.setPreferredName( user.preferredName() );
		entity.setEmail( user.email() );
		entity.setEmailVerified( user.emailVerified() );
		entity.setSmsNumber( user.smsNumber() );
		if( user.smsCarrier() != null ) entity.setSmsCarrier( user.smsCarrier().name().toLowerCase() );
		entity.setSmsVerified( user.smsVerified() );
		entity.setRoles( user.roles() );

		return entity;
	}

	private static User toUserShallow( UserEntity entity ) {
		User user = new User();

		user.id( entity.getId() );
		user.username( entity.getUsername() );
		user.firstName( entity.getFirstName() );
		user.lastName( entity.getLastName() );
		user.preferredName( entity.getPreferredName() );
		user.email( entity.getEmail() );
		user.emailVerified( entity.getEmailVerified() != null && entity.getEmailVerified() );
		user.smsNumber( entity.getSmsNumber() );
		if( Text.isNotBlank( entity.getSmsCarrier() ) ) user.smsCarrier( SmsCarrier.valueOf( entity.getSmsCarrier().toUpperCase() ) );
		user.smsVerified( entity.getSmsVerified() != null && entity.getSmsVerified() );
		user.tokens( entity.getTokens().stream().map( c -> TokenEntity.toUserToken( c ).user( user ) ).collect( Collectors.toSet() ) );
		user.roles( entity.getRoles() );

		return user;
	}

}
