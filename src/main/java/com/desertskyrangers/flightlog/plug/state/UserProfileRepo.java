package com.desertskyrangers.flightlog.plug.state;

import com.desertskyrangers.flightlog.plug.state.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileRepo extends JpaRepository<UserProfileEntity, UUID> {}
