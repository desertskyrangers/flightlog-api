package com.desertskyrangers.flightdeck.adapter.store.entity;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.Location;
import com.desertskyrangers.flightdeck.core.model.Member;
import com.desertskyrangers.flightdeck.util.SmsCarrier;
import com.desertskyrangers.flightdeck.core.model.User;
import com.desertskyrangers.flightdeck.util.Text;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table( name = "\"user\"" )
@Slf4j
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

	@Column( name = "callsign" )
	private String callSign;

	private String email;

	@Column( name = "emailverified" )
	private Boolean emailVerified;

	@Column( name = "smsnumber" )
	private String smsNumber;

	@Column( name = "smscarrier" )
	private String smsCarrier;

	@Column( name = "smsverified" )
	private Boolean smsVerified;

	// This is stored as a projection
	@Column( name = "dashboardid" )
	private UUID dashboardId;

	// This is stored as a projection
	@Column( name = "publicdashboardid" )
	private UUID publicDashboardId;

	@OneToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	@CollectionTable( name = "usertoken", joinColumns = @JoinColumn( name = "userid" ) )
	@EqualsAndHashCode.Exclude
	@ToString.Exclude()
	private Set<TokenEntity> tokens = new HashSet<>();

	@ElementCollection
	@Column( name = "roles", nullable = false )
	@CollectionTable( name = "userrole", joinColumns = @JoinColumn( name = "userid" ) )
	@Fetch( FetchMode.JOIN )
	@EqualsAndHashCode.Exclude
	@ToString.Exclude()
	private Set<String> roles = new HashSet<>();

	@ManyToMany( fetch = FetchType.EAGER )
	@JoinTable( name = "member", joinColumns = @JoinColumn( name = "userid" ), inverseJoinColumns = @JoinColumn( name = "orgid" ) )
	@EqualsAndHashCode.Exclude
	@ToString.Exclude()
	private Set<GroupEntity> groups = new HashSet<>();

	@OneToMany( mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true )
	@EqualsAndHashCode.Exclude
	@ToString.Exclude()
	private Set<MemberEntity> memberships = new HashSet<>();

	@OneToMany( mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true )
	@EqualsAndHashCode.Exclude
	@ToString.Exclude()
	private Set<LocationEntity> locations = new HashSet<>();

	public static UserEntity from( User user ) {
		UserEntity entity = fromUserShallow( user );

		Map<UUID, UserEntity> users = new HashMap<>();
		Map<UUID, GroupEntity> groups = new HashMap<>();
		Map<UUID, TokenEntity> tokens = new HashMap<>();
		entity.setGroups( user.groups().stream().map( g -> GroupEntity.fromGroupFromUser( g, groups, users ) ).collect( Collectors.toSet() ) );
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

	static UserEntity fromUserFromToken( User user, Map<UUID, UserEntity> users, Map<UUID, TokenEntity> tokens ) {
		UserEntity entity = users.get( user.id() );
		if( entity != null ) return entity;

		entity = fromUserShallow( user );
		users.put( user.id(), entity );
		entity.setTokens( user.tokens().stream().map( t -> TokenEntity.fromTokenFromUser( t, tokens, users ) ).collect( Collectors.toSet() ) );

		return entity;
	}

	public static User toUser( UserEntity entity ) {
		User user = toUserShallow( entity );

		final Map<UUID, User> users = new HashMap<>();
		final Map<UUID, Group> groups = new HashMap<>();
		final Map<UUID, Location> locations = new HashMap<>();
		final Map<UUID, Member> members = new HashMap<>();
		users.put( entity.getId(), user );

		user.groups( entity.getGroups().stream().map( e -> GroupEntity.toGroupFromRelated( e, groups, members, locations, users ) ).collect( Collectors.toSet() ) );

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
	static User toUserFromRelated( UserEntity entity, Map<UUID, User> users, Map<UUID, Group> groups, Map<UUID, Location> locations, Map<UUID, Member> members ) {
		// If the user already exists, just return it
		User user = users.get( entity.getId() );
		if( user != null ) return user;

		// Create the shallow version of the user and put it in the users map
		user = toUserShallow( entity );
		users.put( entity.getId(), user );

		// Link the user to related entities
		user.groups( entity.getGroups().stream().map( e -> GroupEntity.toGroupFromRelated( e, groups, members, locations, users ) ).collect( Collectors.toSet() ) );

		return user;
	}

	private static UserEntity fromUserShallow( User user ) {
		UserEntity entity = new UserEntity();

		entity.setId( user.id() );
		entity.setUsername( user.username() );
		entity.setFirstName( user.firstName() );
		entity.setLastName( user.lastName() );
		entity.setPreferredName( user.preferredName() );
		entity.setCallSign( user.callSign() );
		entity.setEmail( user.email() );
		entity.setEmailVerified( user.emailVerified() );
		entity.setSmsNumber( user.smsNumber() );
		entity.setDashboardId( user.dashboardId() );
		entity.setPublicDashboardId( user.publicDashboardId() );
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
		user.callSign( entity.getCallSign() );
		user.email( entity.getEmail() );
		user.emailVerified( entity.getEmailVerified() != null && entity.getEmailVerified() );
		user.smsNumber( entity.getSmsNumber() );
		user.dashboardId( entity.getDashboardId() );
		user.publicDashboardId( entity.getPublicDashboardId() );
		if( Text.isNotBlank( entity.getSmsCarrier() ) ) user.smsCarrier( SmsCarrier.valueOf( entity.getSmsCarrier().toUpperCase() ) );
		user.smsVerified( entity.getSmsVerified() != null && entity.getSmsVerified() );
		user.tokens( entity.getTokens().stream().map( c -> TokenEntity.toUserToken( c ).user( user ) ).collect( Collectors.toSet() ) );
		user.roles( entity.getRoles() );

		return user;
	}

}
