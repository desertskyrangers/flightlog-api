package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.port.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

	@Override
	public List<Group> findGroupsByUser( UUID id ) {
		return List.of();
	}

}
