package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactProfileResponse;
import com.desertskyrangers.flightlog.adapter.api.model.ReactUserAccount;
import com.desertskyrangers.flightlog.core.UserAccountService;
import com.desertskyrangers.flightlog.core.model.SmsCarrier;
import com.desertskyrangers.flightlog.core.model.UserAccount;
import com.desertskyrangers.flightlog.util.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class UserController {

	private final UserAccountService userAccountService;

	public UserController( UserAccountService userAccountService ) {
		this.userAccountService = userAccountService;
	}

	@GetMapping( path = ApiPath.PROFILE )
	ResponseEntity<ReactProfileResponse> profile() {
		String username = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

		Optional<ReactUserAccount> optional = userAccountService.findByPrincipal( username ).map( ReactUserAccount::from );
		if( optional.isEmpty() ) return new ResponseEntity<>( new ReactProfileResponse().setAccount( new ReactUserAccount() ), HttpStatus.BAD_REQUEST );

		return new ResponseEntity<>( new ReactProfileResponse().setAccount( optional.get() ), HttpStatus.OK );
	}

	//	@GetMapping
	//	public List<ReactRegisterRequest> findAll() {
	//		return service.find().stream().map( ReactRegisterRequest::from ).collect( Collectors.toList() );
	//	}

	@GetMapping( value = ApiPath.USER + "/{id}" )
	ResponseEntity<ReactProfileResponse> findById( @PathVariable( "id" ) UUID id ) {
		Optional<ReactUserAccount> optional = userAccountService.find( id ).map( ReactUserAccount::from );
		if( optional.isEmpty() ) return new ResponseEntity<>( new ReactProfileResponse().setAccount( new ReactUserAccount() ), HttpStatus.BAD_REQUEST );

		return new ResponseEntity<>( new ReactProfileResponse().setAccount( optional.get() ), HttpStatus.OK );
	}

	//	@PostMapping
	//	@ResponseStatus( HttpStatus.CREATED )
	//	public UUID create( @RequestBody ReactRegisterRequest resource ) {
	//		//		Preconditions.checkNotNull(resource);
	//		//		return service.create(resource);
	//		return UUID.randomUUID();
	//	}

	@PutMapping( value = ApiPath.USER + "/{id}" )
	@ResponseStatus( HttpStatus.OK )
	public ResponseEntity<ReactProfileResponse> update( @PathVariable( "id" ) UUID id, @RequestBody ReactUserAccount account ) {
		Optional<UserAccount> optional = userAccountService.find( id );
		if( optional.isEmpty() ) return new ResponseEntity<>( new ReactProfileResponse().setAccount( new ReactUserAccount() ), HttpStatus.BAD_REQUEST );

		UserAccount user = account.update( optional.get() );

		// Update the user account
		userAccountService.upsert( user );

		return new ResponseEntity<>( new ReactProfileResponse().setAccount( ReactUserAccount.from( user ) ), HttpStatus.OK );
	}

	@DeleteMapping( value = ApiPath.USER + "/{id}" )
	@ResponseStatus( HttpStatus.OK )
	public void delete( @PathVariable( "id" ) UUID id ) {
		userAccountService.find( id ).ifPresent( userAccountService::remove );
	}

}
