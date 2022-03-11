package com.desertskyrangers.flightdeck.port;

import com.desertskyrangers.flightdeck.core.model.User;

public interface CommonDashboard<T> {

	T update( User user );

}
