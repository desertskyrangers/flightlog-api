package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.PublicDashboard;
import com.desertskyrangers.flightdeck.core.model.User;

import java.util.Optional;
import java.util.UUID;

public interface PublicDashboardServices {

	Optional<PublicDashboard> find( UUID uuid );

	Optional<PublicDashboard> findByUser( User user );

	PublicDashboard upsert( User user, PublicDashboard dashboard );

	PublicDashboard update( User user );

}
