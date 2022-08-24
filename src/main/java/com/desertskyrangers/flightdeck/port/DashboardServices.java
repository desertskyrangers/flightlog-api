package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.User;

public interface DashboardServices {

	String update( User user );

	String update( Group group );


}
