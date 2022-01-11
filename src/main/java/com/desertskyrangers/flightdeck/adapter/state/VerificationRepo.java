package com.desertskyrangers.flightdeck.adapter.state;

import com.desertskyrangers.flightdeck.adapter.state.entity.VerificationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface VerificationRepo extends CrudRepository<VerificationEntity, UUID> {}
