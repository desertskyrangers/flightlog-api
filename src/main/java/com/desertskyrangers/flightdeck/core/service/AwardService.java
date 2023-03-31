package com.desertskyrangers.flightdeck.core.service;

import com.desertskyrangers.flightdeck.core.model.Award;
import com.desertskyrangers.flightdeck.core.model.Battery;
import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AwardService {

	public Page<Award> getAwards( UUID id ) {
		return Page.empty();
	}

	public void checkForAwards( User user ) {
		// We want to determine if a user is eligible for one or more awards
		// - Check for records
		// - Check for flight awards
		// - Check for special awards

		// Create awards
	}

	public void checkForAwards( Group group ) {
		// We want to determine if a group is eligible for one or more awards
		// - Check for flight awards
		// - Check for special awards

		// Create awards
	}

	public void createAward( Award award ) {
		//
	}

}
