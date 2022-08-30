package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.User;

public interface DashboardServices {

	void update( User user );

	void update( Group group );

}
