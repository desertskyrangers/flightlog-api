package com.desertskyrangers.flightdeck.adapter.state.repo;

import com.desertskyrangers.flightdeck.adapter.state.entity.PreferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PreferencesRepo extends JpaRepository<PreferencesEntity, UUID> {}
