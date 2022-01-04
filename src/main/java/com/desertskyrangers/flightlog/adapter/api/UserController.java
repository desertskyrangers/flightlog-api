package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactProfileResponse;
import com.desertskyrangers.flightlog.adapter.api.model.ReactRegisterRequest;
import com.desertskyrangers.flightlog.adapter.api.model.ReactUserAccount;
import com.desertskyrangers.flightlog.core.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping( "/api/user" )
public class UserController {

	private final UserAccountService userAccountService;

	public UserController( UserAccountService userAccountService ) {
		this.userAccountService = userAccountService;
	}

	@GetMapping( path = ApiPath.PROFILE )
	ResponseEntity<ReactProfileResponse> profile() {
		String username = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		Optional<ReactUserAccount> optional = userAccountService.findByUsername( username ).map( ReactUserAccount::from );
		if( optional.isEmpty() ) return new ResponseEntity<>( new ReactProfileResponse().setAccount( new ReactUserAccount() ), HttpStatus.OK );

		return new ResponseEntity<>( new ReactProfileResponse().setAccount( optional.get() ), HttpStatus.OK );
	}

	//	@GetMapping
	//	public List<ReactRegisterRequest> findAll() {
	//		return service.find().stream().map( ReactRegisterRequest::from ).collect( Collectors.toList() );
	//	}

	@GetMapping( value = "/{id}" )
	public ReactRegisterRequest findById( @PathVariable( "id" ) Long id ) {
		//return RestPreconditions.checkFound(service.findById(id));
		return new ReactRegisterRequest();
	}

	@PostMapping
	@ResponseStatus( HttpStatus.CREATED )
	public UUID create( @RequestBody ReactRegisterRequest resource ) {
		//		Preconditions.checkNotNull(resource);
		//		return service.create(resource);
		return UUID.randomUUID();
	}

	@PutMapping( value = "/{id}" )
	@ResponseStatus( HttpStatus.OK )
	public void update( @PathVariable( "id" ) Long id, @RequestBody ReactRegisterRequest resource ) {
		//		Preconditions.checkNotNull(resource);
		//		RestPreconditions.checkNotNull(service.getById(resource.getId()));
		//		service.update(resource);
	}

	@DeleteMapping( value = "/{id}" )
	@ResponseStatus( HttpStatus.OK )
	public void delete( @PathVariable( "id" ) Long id ) {
		//service.deleteById(id);
	}

}
