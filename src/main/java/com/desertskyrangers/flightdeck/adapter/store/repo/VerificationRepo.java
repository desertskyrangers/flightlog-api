package com.desertskyrangers.flightdeck.adapter.store.repo;

import com.desertskyrangers.flightdeck.adapter.store.entity.VerificationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface VerificationRepo extends CrudRepository<VerificationEntity, UUID> {}
