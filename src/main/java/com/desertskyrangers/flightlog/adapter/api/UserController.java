package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.core.UserAccountService;
import com.desertskyrangers.flightlog.adapter.api.model.ReactUserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping( "/api/user" )
public class UserController {

	private final UserAccountService service;

	public UserController( UserAccountService service ) {
		this.service = service;
	}

	@GetMapping
	public List<ReactUserAccount> findAll() {
		return service.find().stream().map( ReactUserAccount::from ).collect( Collectors.toList() );
	}

	@GetMapping( value = "/{id}" )
	public ReactUserAccount findById( @PathVariable( "id" ) Long id ) {
		//return RestPreconditions.checkFound(service.findById(id));
		return new ReactUserAccount();
	}

	@PostMapping
	@ResponseStatus( HttpStatus.CREATED )
	public UUID create( @RequestBody ReactUserAccount resource ) {
		//		Preconditions.checkNotNull(resource);
		//		return service.create(resource);
		return UUID.randomUUID();
	}

	@PutMapping( value = "/{id}" )
	@ResponseStatus( HttpStatus.OK )
	public void update( @PathVariable( "id" ) Long id, @RequestBody ReactUserAccount resource ) {
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
