package com.desertskyrangers.flightlog.plug.api;

public interface ApiPath {

	String ROOT = "/api";

	String AUTH = ROOT + "/auth";

	String AUTH_CSRF = AUTH + "/csrf";

	String AUTH_LOGIN = AUTH + "/login";

	String AUTH_SIGNUP = AUTH + "/signup";

	String MONITOR = ROOT + "/monitor";

	String MONITOR_STATUS = MONITOR + "/status";

}
