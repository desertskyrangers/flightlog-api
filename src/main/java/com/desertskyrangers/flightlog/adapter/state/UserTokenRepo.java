package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.adapter.state.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserTokenRepo extends JpaRepository<TokenEntity, UUID> {

	Optional<TokenEntity> findByPrincipal( String principal );

}
