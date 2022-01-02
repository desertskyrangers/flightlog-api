package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.adapter.state.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialRepo extends JpaRepository<CredentialEntity, UUID> {

	Optional<CredentialEntity> findByUsername( String username );

}
