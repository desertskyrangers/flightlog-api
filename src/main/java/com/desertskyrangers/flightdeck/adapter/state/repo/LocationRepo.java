package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepo extends JpaRepository<LocationEntity, UUID> {}
