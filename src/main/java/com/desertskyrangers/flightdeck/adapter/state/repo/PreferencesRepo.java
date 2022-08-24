package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.PreferencesProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Deprecated
public interface PreferencesRepo extends JpaRepository<PreferencesProjection, UUID> {}
