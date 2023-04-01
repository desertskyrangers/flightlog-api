package com.desertskyrangers.flightdeck.adapter.store.repo;

import com.desertskyrangers.flightdeck.adapter.store.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<UserEntity, UUID> {}
