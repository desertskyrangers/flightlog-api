package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.User;

import java.util.*;

public interface UserServices {

	void setDashboardServices( DashboardServices dashboardServices );

	Set<User> find();

	Optional<User> find( UUID id );

	Optional<User> findByPrincipal( String username );

	void upsert( User user );

	void remove( User user );

	boolean isCurrentPassword( User user, String password );

	void updatePassword( User user, String password );

	Optional<User> findVerificationUser( UUID verificationId );

	Set<User> findAllGroupPeers( User user );

	Set<User> findAllAcceptedGroupPeers( User user );

	Map<String, Object> getPreferences( User user );

	User setPreferences( User user, Map<String, Object> preferences );

}
