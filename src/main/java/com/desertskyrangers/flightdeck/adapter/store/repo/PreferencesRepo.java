package com.desertskyrangers.flightdeck.adapter.store.repo;

import com.desertskyrangers.flightdeck.adapter.store.entity.PreferencesProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Deprecated
public interface PreferencesRepo extends JpaRepository<PreferencesProjection, UUID> {}
