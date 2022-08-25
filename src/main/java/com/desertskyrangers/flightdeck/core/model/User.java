package com.desertskyrangers.flightdeck.core.model;

import com.desertskyrangers.flightdeck.util.Text;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
@Accessors( fluent = true )
public class User implements Comparable<User> {

	private UUID id = UUID.randomUUID();

	private String username;

	private String firstName;

	private String lastName;

	private String preferredName;

	private String callSign;

	private String email;

	private boolean emailVerified;

	private String smsNumber;

	private SmsCarrier smsCarrier;

	private boolean smsVerified;

	private UUID dashboardId;

	private UUID publicDashboardId;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<UserToken> tokens = new CopyOnWriteArraySet<>();

	@EqualsAndHashCode.Exclude
	private Set<String> roles = new CopyOnWriteArraySet<>();

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<Group> groups = new CopyOnWriteArraySet<>();

	public String name() {
		String name = preferredName();
		if( Text.isBlank( name ) && Text.isNotBlank( callSign() ) ) name = firstName() + " \"" + callSign() + "\" " + lastName();
		if( Text.isBlank( name ) ) name = firstName() + " " + lastName();
		return name.trim();
	}

	public void roles( Set<String> roles ) {
		this.roles = roles == null ? Set.of() : roles;
	}

	@Override
	public int compareTo( User that ) {
		return this.name().compareTo( that.name() );
	}

}
