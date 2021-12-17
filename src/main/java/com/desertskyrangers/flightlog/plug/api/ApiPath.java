package com.desertskyrangers.flightlog.plug.api;

public interface ApiPath {

	String ROOT = "/api";

	String AUTH = ROOT + "/auth";

	String SIGNUP = AUTH + "/signup";

	String MONITOR = ROOT + "/monitor";

	String MONITOR_STATUS = MONITOR + "/status";

}
