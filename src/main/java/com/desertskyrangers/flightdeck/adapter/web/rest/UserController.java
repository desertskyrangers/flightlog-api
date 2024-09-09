package com.desertskyrangers.flightdeck.adapter.web.rest;

import com.desertskyrangers.flightdeck.adapter.web.ApiPath;
import com.desertskyrangers.flightdeck.adapter.web.model.*;
import com.desertskyrangers.flightdeck.core.exception.UnauthorizedException;
import com.desertskyrangers.flightdeck.core.model.*;
import com.desertskyrangers.flightdeck.port.*;
import com.desertskyrangers.flightdeck.util.Uuid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.PermitAll;
import java.util.*;

@RestController
//@RequestMapping( produces = "application/json" )
@Slf4j
public class UserController extends BaseController {

	private final AircraftServices aircraftServices;

	private final BatteryServices batteryServices;

	private final FlightServices flightServices;

	private final GroupServices groupServices;

	private final LocationServices locationServices;

	private final ProjectionServices projectionServices;

	private final UserServices userServices;

	private final MembershipServices memberService;

	public UserController(
		AircraftServices aircraftServices,
		BatteryServices batteryServices,
		FlightServices flightServices,
		GroupServices groupServices,
		LocationServices locationServices,
		MembershipServices memberService,
		ProjectionServices projectionServices,
		UserServices userServices
	) {
		this.aircraftServices = aircraftServices;
		this.batteryServices = batteryServices;
		this.flightServices = flightServices;
		this.groupServices = groupServices;
		this.locationServices = locationServices;
		this.memberService = memberService;
		this.projectionServices = projectionServices;
		this.userServices = userServices;
	}

	@PermitAll
	@GetMapping( path = ApiPath.PUBLIC_DASHBOARD + "/{id}" )
	ResponseEntity<?> publicDashboard( @PathVariable String id ) {
		List<String> messages = new ArrayList<>();

		try {
			Optional<User> optionalUser = userServices.findByPrincipal( id );
			if( optionalUser.isEmpty() && Uuid.isValid( id ) ) optionalUser = userServices.find( UUID.fromString( id ) );
			if( optionalUser.isEmpty() ) messages.add( "Dashboard not found" );
			if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( List.of( "Dashboard not found" ) ), HttpStatus.BAD_REQUEST );

			User dashboardOwner = optionalUser.get();
			Map<String, Object> preferences = userServices.getPreferences( dashboardOwner );

			boolean showPublicDashboard = Objects.equals( String.valueOf( preferences.get( PreferenceKey.ENABLE_PUBLIC_DASHBOARD ) ), "true" );
			if( !showPublicDashboard ) return new ResponseEntity<>( ReactResponse.messages( List.of( "Dashboard not found" ) ), HttpStatus.BAD_REQUEST );

			// Get and verify the dashboard
			Optional<String> projection = projectionServices.findProjection( dashboardOwner.publicDashboardId() );
			if( projection.isEmpty() ) messages.add( "Dashboard not found" );
			if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

			return new ResponseEntity<>( ReactResponse.wrapProjection( projection.get() ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error retrieving dashboard", exception );
			return new ResponseEntity<>( ReactResponse.messages( List.of( "Error retrieving dashboard" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.DASHBOARD )
	ResponseEntity<?> dashboard( Authentication authentication ) {
		List<String> messages = new ArrayList<>();

		try {
			User requester = getRequester( authentication );

			// Get and verify the dashboard
			Optional<String> projection = projectionServices.findProjection( requester.dashboardId() );
			if( projection.isEmpty() ) messages.add( "Dashboard not found" );
			if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

			return new ResponseEntity<>( ReactResponse.wrapProjection( projection.get() ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error retrieving dashboard", exception );
			return new ResponseEntity<>( ReactResponse.messages( List.of( "Error retrieving dashboard" ) ), HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.PROFILE )
	ResponseEntity<ReactProfileResponse> profile() {
		String username = ((org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

		Optional<ReactUser> optional = userServices.findByPrincipal( username ).map( ReactUser::from );
		if( optional.isEmpty() ) return new ResponseEntity<>( new ReactProfileResponse().setAccount( new ReactUser() ), HttpStatus.BAD_REQUEST );

		return new ResponseEntity<>( new ReactProfileResponse().setAccount( optional.get() ), HttpStatus.OK );
	}

	// FIXME Normal users should not be able to update other users
	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( value = ApiPath.USER + "/{id}" )
	ResponseEntity<ReactProfileResponse> findById( @PathVariable( "id" ) UUID id ) {
		Optional<ReactUser> optional = userServices.find( id ).map( ReactUser::from );
		if( optional.isEmpty() ) return new ResponseEntity<>( new ReactProfileResponse().setAccount( new ReactUser() ), HttpStatus.BAD_REQUEST );

		return new ResponseEntity<>( new ReactProfileResponse().setAccount( optional.get() ), HttpStatus.OK );
	}

	// FIXME Normal users should not be able to update other users
	@PreAuthorize( "hasAuthority('USER')" )
	@PutMapping( value = ApiPath.USER + "/{id}" )
	ResponseEntity<ReactProfileResponse> update( @PathVariable( "id" ) UUID id, @RequestBody ReactUser request ) {
		Optional<User> optional = userServices.find( id );
		if( optional.isEmpty() ) return new ResponseEntity<>( new ReactProfileResponse().setAccount( new ReactUser() ), HttpStatus.BAD_REQUEST );

		User user = request.updateFrom( optional.get() );

		// Update the user
		userServices.upsert( user );

		return new ResponseEntity<>( new ReactProfileResponse().setAccount( ReactUser.from( user ) ), HttpStatus.OK );
	}

	// FIXME Normal users should not be able to update other users
	@PreAuthorize( "hasAuthority('USER')" )
	@DeleteMapping( value = ApiPath.USER + "/{id}" )
	@ResponseStatus( HttpStatus.OK )
	void delete( @PathVariable( "id" ) UUID id ) {
		userServices.find( id ).ifPresent( userServices::remove );
	}

	// FIXME Normal users should not be able to update other users
	@PreAuthorize( "hasAuthority('USER')" )
	@PutMapping( value = ApiPath.USER + "/{id}/password" )
	@ResponseStatus( HttpStatus.OK )
	ResponseEntity<Map<String, Object>> updatePassword( @PathVariable( "id" ) UUID id, @RequestBody ReactPasswordChangeRequest request ) {
		// Check that ids match
		if( !Objects.equals( id.toString(), request.getId() ) ) return new ResponseEntity<>( Map.of( "messages", List.of( "User id mismatch" ) ), HttpStatus.BAD_REQUEST );

		// Check the user exists
		Optional<User> optional = userServices.find( id );
		if( optional.isEmpty() ) return new ResponseEntity<>( Map.of( "messages", List.of( "User not found: " + id ) ), HttpStatus.BAD_REQUEST );

		// Check that passwords match
		if( !userServices.isCurrentPassword( optional.get(), request.getCurrentPassword() ) ) {
			return new ResponseEntity<>( Map.of( "messages", List.of( "Current password mismatch" ) ), HttpStatus.BAD_REQUEST );
		}

		// Update the user account
		userServices.updatePassword( optional.get(), request.getPassword() );

		return new ResponseEntity<>( HttpStatus.OK );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_AIRCRAFT )
	ResponseEntity<ReactPageResponse<?>> getAircraftPage(
		Authentication authentication,
		@RequestParam( value = "filter", defaultValue = "available" ) String filter,
		@RequestParam( value = "pg", defaultValue = "0" ) int page,
		@RequestParam( value = "pz", defaultValue = DEFAULT_PAGE_SIZE ) int size
	) {
		List<String> messages = new ArrayList<>();

		// boolean airworthy only?
		// boolean not-airworthy only?
		// string state filter?

		Set<Aircraft.Status> status = Set.of( Aircraft.Status.PREFLIGHT, Aircraft.Status.AIRWORTHY );
		if( "unavailable".equals( filter ) ) status = Set.of( Aircraft.Status.INOPERATIVE, Aircraft.Status.DECOMMISSIONED, Aircraft.Status.DESTROYED );

		try {
			User user = getRequester( authentication );
			Page<ReactAircraft> aircraftPage = aircraftServices.findPageByOwnerAndStatus( user.id(), status, page, size ).map( ReactAircraft::from );
			return new ResponseEntity<>( ReactPageResponse.of( aircraftPage ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error getting aircraft page", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactPageResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_BATTERY )
	ResponseEntity<ReactPageResponse<?>> getBatteryPage(
		Authentication authentication,
		@RequestParam( value = "filter", defaultValue = "available" ) String filter,
		@RequestParam( value = "pg", defaultValue = "0" ) int page,
		@RequestParam( value = "pz", defaultValue = DEFAULT_PAGE_SIZE ) int size
	) {
		List<String> messages = new ArrayList<>();

		Set<Battery.Status> status = Set.of( Battery.Status.NEW, Battery.Status.AVAILABLE );
		if( "unavailable".equals( filter ) ) status = Set.of( Battery.Status.DESTROYED );

		try {
			User user = getRequester( authentication );
			Page<ReactBattery> batteryPage = batteryServices.findPageByOwnerAndStatus( user.id(), status, page, size ).map( ReactBattery::from );
			return new ResponseEntity<>( ReactPageResponse.of( batteryPage ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error getting battery page", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactPageResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_FLIGHT )
	ResponseEntity<ReactPageResponse<?>> getFlightPage(
		Authentication authentication, @RequestParam( value = "pg", defaultValue = "0" ) int page, @RequestParam( value = "pz", defaultValue = DEFAULT_PAGE_SIZE ) int size
	) {
		List<String> messages = new ArrayList<>();

		try {
			User requester = getRequester( authentication );
			Page<ReactFlight> flightPage = flightServices.findFlightsByUser( requester, page, size ).map( f -> ReactFlight.from( requester, f ) );
			return new ResponseEntity<>( ReactPageResponse.of( flightPage ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error getting flight page", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactPageResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_GROUP )
	ResponseEntity<ReactPageResponse<?>> getGroupPage(
		Authentication authentication, @RequestParam( value = "pg", defaultValue = "0" ) int page, @RequestParam( value = "pz", defaultValue = DEFAULT_PAGE_SIZE ) int size
	) {
		List<String> messages = new ArrayList<>();

		try {
			User user = getRequester( authentication );
			Page<ReactGroup> groupPage = groupServices.findGroupsPageByUser( user, page, size ).map( ReactGroup::from );
			return new ResponseEntity<>( ReactPageResponse.of( groupPage ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error getting group page", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactPageResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_LOCATION )
	ResponseEntity<ReactPageResponse<?>> getLocationPage(
		Authentication authentication,
		@RequestParam( value = "filter", defaultValue = "active" ) String filter,
		@RequestParam( value = "pg", defaultValue = "0" ) int page,
		@RequestParam( value = "pz", defaultValue = DEFAULT_PAGE_SIZE ) int size
	) {
		List<String> messages = new ArrayList<>();

		Set<Location.Status> status = Set.of( Location.Status.ACTIVE );
		if( "inactive".equals( filter ) ) status = Set.of( Location.Status.REMOVED );
		if( Location.Status.REMOVED.name().toLowerCase().equals( filter ) ) status = Set.of( Location.Status.REMOVED );

		try {
			User user = getRequester( authentication );
			if( size == 0 ) {
				List<ReactLocation> locationList = locationServices.findByUserAndStatus( user, status ).stream().map( ReactLocation::from ).sorted().toList();
				Page<ReactLocation> locationPage = new PageImpl<>(locationList);
				return new ResponseEntity<>( ReactPageResponse.of( locationPage ), HttpStatus.OK );
			} else {
				Page<ReactLocation> locationPage = locationServices.findPageByUserAndStatus( user, status, page, size ).map( ReactLocation::from );
				return new ResponseEntity<>( ReactPageResponse.of( locationPage ), HttpStatus.OK );
			}
		} catch( Exception exception ) {
			log.error( "Error getting location page", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactPageResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_MEMBERSHIP )
	ResponseEntity<ReactResponse<?>> getMembershipPage( Authentication authentication ) {
		List<String> messages = new ArrayList<>();

		try {
			User user = getRequester( authentication );
			return new ResponseEntity<>( ReactResponse.of( getMemberships( user ) ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error getting membership page", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	/**
	 * This retrieves the list of aircraft to show in the flight aircraft drop-down
	 *
	 * @param authentication The user authentication
	 * @return The list of airworthy aircraft to show in the flight aircraft drop-down
	 */
	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_AIRCRAFT_LOOKUP )
	ResponseEntity<List<ReactOption>> getAircraftLookup( Authentication authentication ) {
		User user = getRequester( authentication );
		List<Aircraft> objects = aircraftServices.findAllByOwner( user.id() );
		return new ResponseEntity<>( objects.stream().filter( a -> a.status().isAirworthy() ).map( c -> new ReactOption( c.id().toString(), c.name() ) ).toList(), HttpStatus.OK );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_BATTERY_LOOKUP )
	ResponseEntity<List<ReactOption>> getBatteryLookup( Authentication authentication ) {
		User user = getRequester( authentication );
		List<Battery> objects = batteryServices.findByOwner( user.id() );
		List<ReactOption> options = new ArrayList<>( objects.stream().filter( b -> b.status().isAirworthy() ).map( c -> new ReactOption( c.id().toString(), c.name() ) ).toList() );
		options.add( new ReactOption( "", "No battery specified" ) );
		return new ResponseEntity<>( options, HttpStatus.OK );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_LOCATION_LOOKUP )
	ResponseEntity<List<ReactOption>> getLocationLookup( Authentication authentication ) {
		User user = getRequester( authentication );
		Set<Location> objects = locationServices.findByUser( user );
		List<ReactOption> options = new ArrayList<>( objects.stream().map( c -> new ReactOption( c.id().toString(), c.name() ) ).toList() );

		options.add( new ReactOption( Location.DEVICE_LOCATION_ID.toString(), "Use device location" ) );
		options.add( new ReactOption( Location.CUSTOM_LOCATION_ID.toString(), "Use custom location" ) );
		options.add( new ReactOption( Location.NO_LOCATION_ID.toString(), "No location specified" ) );

		return new ResponseEntity<>( options, HttpStatus.OK );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_PILOT_LOOKUP )
	ResponseEntity<List<ReactOption>> getPilotLookup( Authentication authentication ) {
		User user = getRequester( authentication );

		List<User> users = new ArrayList<>( userServices.findAllAcceptedGroupPeers( user ) );
		users.sort( null );
		users.add( 0, user );
		users.add( unlistedUser() );

		return new ResponseEntity<>( users.stream().map( c -> new ReactOption( c.id().toString(), c.name() ) ).toList(), HttpStatus.OK );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_OBSERVER_LOOKUP )
	ResponseEntity<List<ReactOption>> getObserverLookup( Authentication authentication ) {
		return getPilotLookup( authentication );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@PutMapping( path = ApiPath.USER_MEMBERSHIP )
	ResponseEntity<ReactMembershipPageResponse> requestGroupMembership( Authentication authentication, @RequestBody Map<String, String> request ) {
		String userId = request.get( "userid" );
		String groupId = request.get( "groupid" );
		String statusKey = request.get( "status" );

		List<String> messages = new ArrayList<>();
		if( Uuid.isNotValid( userId ) ) messages.add( "Invalid user ID" );
		if( Uuid.isNotValid( groupId ) ) messages.add( "Invalid group ID" );
		if( Member.Status.isNotValid( statusKey ) ) messages.add( "Invalid membership status" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactMembershipPageResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			User requester = getRequester( authentication );
			User user = userServices.find( UUID.fromString( userId ) ).orElse( null );
			Group group = groupServices.find( UUID.fromString( groupId ) ).orElse( null );
			Member.Status status = Member.Status.valueOf( statusKey.toUpperCase() );
			if( user == null ) {
				messages.add( "User not found" );
				log.warn( "User not found id=" + userId );
			}
			if( group == null ) {
				messages.add( "Group not found" );
				log.warn( "Group not found id=" + groupId );
			}
			if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactMembershipPageResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

			memberService.requestMembership( requester, user, group, status );

			return new ResponseEntity<>( new ReactMembershipPageResponse().setMemberships( getMemberships( user ) ), HttpStatus.OK );
		} catch( UnauthorizedException exception ) {
			return new ResponseEntity<>( new ReactMembershipPageResponse().setMessages( List.of( "Unauthorized" ) ), HttpStatus.UNAUTHORIZED );
		} catch( Exception exception ) {
			log.error( "Error requesting group membership", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactMembershipPageResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@DeleteMapping( path = ApiPath.USER_MEMBERSHIP )
	ResponseEntity<ReactMembershipPageResponse> cancelGroupMembership( Authentication authentication, @RequestBody Map<String, String> request ) {
		String userId = request.get( "membershipid" );

		List<String> messages = new ArrayList<>();
		if( Uuid.isNotValid( userId ) ) messages.add( "Invalid membership ID" );
		if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactMembershipPageResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

		try {
			User requester = getRequester( authentication );
			Member member = memberService.find( UUID.fromString( userId ) ).orElse( null );
			if( member == null ) {
				messages.add( "Membership not found" );
				log.warn( "Membership not found id={}", userId );
			}
			if( !messages.isEmpty() ) return new ResponseEntity<>( new ReactMembershipPageResponse().setMessages( messages ), HttpStatus.BAD_REQUEST );

			User user = member.user();
			memberService.cancelMembership( requester, member );

			return new ResponseEntity<>( new ReactMembershipPageResponse().setMemberships( getMemberships( user ) ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error cancelling group membership", exception );
			messages.add( exception.getMessage() );
		}

		return new ResponseEntity<>( new ReactMembershipPageResponse().setMessages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@GetMapping( path = ApiPath.USER_PREFERENCES )
	ResponseEntity<ReactResponse<Map<String, Object>>> getPreferencesByRequester( Authentication authentication ) {
		return getPreferences( getRequester( authentication ).id().toString() );
	}

	@PreAuthorize( "hasAuthority('ADMIN')" )
	@GetMapping( path = ApiPath.USER_PREFERENCES + "/{id}" )
	ResponseEntity<ReactResponse<Map<String, Object>>> getPreferences( @PathVariable String id ) {
		List<String> messages = new ArrayList<>();

		try {
			if( Uuid.isNotValid( id ) ) messages.add( "Invalid user ID" );
			if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

			// TODO Verify the requester access the users preferences

			Optional<User> optional = userServices.find( UUID.fromString( id ) );
			if( optional.isEmpty() ) {
				messages.add( "User not found" );
				log.warn( "User not found id={}", id );
			}
			if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

			return new ResponseEntity<>( ReactResponse.of( userServices.getPreferences( optional.get() ) ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error retrieving user preferences", exception );
			messages.add( "Error retrieving user preferences" );
			return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}

	@PreAuthorize( "hasAuthority('USER')" )
	@PutMapping( path = ApiPath.USER_PREFERENCES )
	ResponseEntity<ReactResponse<Map<String, Object>>> setPreferences( @RequestBody Map<String, Object> request ) {
		List<String> messages = new ArrayList<>();

		try {
			String id = String.valueOf( request.get( "id" ) );
			Object preferencesObject = request.get( "preferences" );
			if( Uuid.isNotValid( id ) ) messages.add( "Invalid user ID" );
			if( !(preferencesObject instanceof Map) ) messages.add( "Invalid preferences map" );
			if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

			Optional<User> optional = userServices.find( UUID.fromString( id ) );
			if( optional.isEmpty() ) {
				messages.add( "User not found" );
				log.warn( "User not found id={}", id );
			}
			if( !messages.isEmpty() ) return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.BAD_REQUEST );

			User user = optional.get();
			//noinspection unchecked
			Map<String, Object> preferences = (Map<String, Object>)preferencesObject;
			userServices.setPreferences( user, preferences );

			return new ResponseEntity<>( ReactResponse.of( preferences ), HttpStatus.OK );
		} catch( Exception exception ) {
			log.error( "Error storing user preferences", exception );
			messages.add( "Error storing user preferences" );
			return new ResponseEntity<>( ReactResponse.messages( messages ), HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}

	private List<ReactMembership> getMemberships( User user ) {
		Set<Member> memberships = memberService.findMembershipsByUser( user );
		List<Member> objects = new ArrayList<>( memberships );
		Collections.sort( objects );
		return objects.stream().map( ReactMembership::from ).toList();
	}

}
