package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.adapter.state.entity.UserCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialRepo extends JpaRepository<UserCredentialEntity, UUID> {

	Optional<UserCredentialEntity> findByUsername( String username);

}
