package com.desertskyrangers.flightdeck.adapter.store.repo;

import com.desertskyrangers.flightdeck.adapter.store.entity.ProjectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectionRepo extends JpaRepository<ProjectionEntity, UUID> {

	//

}
