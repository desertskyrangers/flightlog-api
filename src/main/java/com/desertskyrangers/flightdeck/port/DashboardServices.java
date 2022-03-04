package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Dashboard;
import com.desertskyrangers.flightdeck.core.model.User;

import java.util.Optional;
import java.util.UUID;

public interface DashboardServices {

	Optional<Dashboard> find( UUID uuid );

	Optional<Dashboard> findByUser( User user );

	Dashboard upsert( User user, Dashboard dashboard );

	Dashboard update( User user );

}
