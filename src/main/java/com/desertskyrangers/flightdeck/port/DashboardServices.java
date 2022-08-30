package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.Group;
import com.desertskyrangers.flightdeck.core.model.User;

import java.util.concurrent.Future;

public interface DashboardServices {

	Future<String> update( User user );

	Future<String> update( Group group );

}
