package com.desertskyrangers.flightlog.plug.state;

import com.desertskyrangers.flightlog.plug.state.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserAccountRepo extends JpaRepository<UserAccountEntity, UUID> {}
