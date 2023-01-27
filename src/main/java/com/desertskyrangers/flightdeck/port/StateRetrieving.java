package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.*;
import org.springframework.data.domain.Page;

import java.util.*;

public interface StateRetrieving {

	Optional<Aircraft> findAircraft( UUID id );

	List<Aircraft> findAircraftByOwner( UUID id );

	Page<Aircraft> findAircraftPageByOwner( UUID owner, int page, int size );

	Page<Aircraft> findAircraftPageByOwnerAndStatus( UUID owner, Set<Aircraft.Status> status, int page, int size );

	List<Aircraft> findAircraftByOwnerAndStatus( UUID id, Aircraft.Status status );

	List<Aircraft> findAllAircraft();

	Optional<Battery> findBattery( UUID id );

	List<Battery> findBatteriesByOwner( UUID id );

	Page<Battery> findBatteriesPageByOwner( UUID id, int page, int size );

	Page<Battery> findBatteriesPageByOwnerAndStatus( UUID owner, Set<Battery.Status> status, int page, int size );

	List<Battery> findAllBatteries();

	Optional<Flight> findFlight( UUID id );

	List<Flight> findFlightsByPilot( UUID id );

	Page<Flight> findFlightsPageByPilot( UUID id, int page, int size );

	List<Flight> findFlightsByPilotAndTimestampAfter( UUID id, long timestamp );

	Page<Flight> findFlightsPageByObserver( UUID id, int page, int size );

	List<Flight> findFlightsByObserverAndTimestampAfter( UUID id, long timestamp );

	Page<Flight> findFlightsPageByOwner( UUID id, int page, int size );

	List<Flight> findFlightsByOwnerAndTimestampAfter( UUID id, long timestamp );

	List<Flight> findFlightsByPilotAndCount( UUID id, int count );

	List<Flight> findFlightsByObserverAndCount( UUID id, int count );

	List<Flight> findFlightsByOwnerAndCount( UUID id, int count );

	List<Flight> findFlightsByAircraft( UUID id );

	List<Flight> findFlightsByBattery( UUID id );

	Page<Flight> findFlightsByPilotObserverOwner( User pilot, User observer, User owner, int page, int size );

	List<Flight> findAllFlights();

	Set<Group> findAllGroups();

	Set<Group> findAllAvailableGroups( User user );

	Optional<Group> findGroup( UUID id );

	Set<Group> findGroupsByOwner( User user );

	Page<Group> findGroupsPageByOwner( User user, int page, int size );

	Set<User> findGroupOwners( Group group );

	Set<Location> findAllLocations();

	Set<Location> findAllActiveLocations( User user );

	Optional<Location> findLocation( UUID id );

	Set<Location> findLocationsByUser( User user );
	Set<Location> findLocationsByUserAndStatus( User user, Set<Location.Status> status );

	Page<Location> findLocationsPageByUser( User user, int page, int size );

	Optional<Member> findMembership( UUID id );

	Optional<Member> findMembership( Group group, User user );

	Set<Member> findMemberships( User user );

	Set<Member> findMemberships( Group group );

	Map<String, Object> findPreferences( User user );

	Optional<UserToken> findUserToken( UUID id );

	Optional<UserToken> findUserTokenByPrincipal( String username );

	Set<User> findAllUsers();

	Optional<User> findUser( UUID id );

	List<Verification> findAllVerifications();

	Optional<Verification> findVerification( UUID id );

	int getPilotFlightCount( User user );

	long getPilotFlightTime( User user );

	int getObserverFlightCount( User user );

	long getObserverFlightTime( User user );

	Optional<Flight> getLastAircraftFlight( Aircraft aircraft );

	Optional<Flight> getLastPilotFlight( User pilot );

	int getAircraftFlightCount( Aircraft aircraft );

	long getAircraftFlightTime( Aircraft aircraft );

	int getBatteryFlightCount( Battery battery );

	long getBatteryFlightTime( Battery battery );

	Optional<Flight> getFlightWithLongestTime( User user );

	Optional<Flight> getFlightWithLongestTime( Aircraft aircraft );

	boolean isPreferenceSet( User user, String key );

	boolean isPreferenceSetTo( User user, String key, String value );

	String getPreference( User user, String key );

	String getPreference( User user, String key, String defaultValue );

	Optional<String> findProjection( UUID id );

}
