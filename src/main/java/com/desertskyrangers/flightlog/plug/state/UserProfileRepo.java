package com.desertskyrangers.flightlog.plug.state;

import com.desertskyrangers.flightlog.plug.state.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepo extends JpaRepository<UserProfileEntity, Long> {}
