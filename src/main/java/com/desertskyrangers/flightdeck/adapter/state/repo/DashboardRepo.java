package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.DashboardProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DashboardRepo extends JpaRepository<DashboardProjection, UUID> {}
