package com.desertskyrangers.flightlog.adapter.state;

import com.desertskyrangers.flightlog.adapter.state.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileRepo extends JpaRepository<UserProfileEntity, UUID> {}
