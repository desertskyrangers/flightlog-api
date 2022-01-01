package com.desertskyrangers.flightlog.adapter.api;

public interface ApiPath {

	String HOST = "https://flightlog.desertskyrangers.com";

	String ROOT = "/api";

	String AUTH = ROOT + "/auth";

	String AUTH_REGISTER = AUTH + "/register";

	String AUTH_VERIFY = AUTH + "/verify";

	String AUTH_LOGIN = AUTH + "/login";

	String AUTH_LOGOUT = AUTH + "/logout";

	String MONITOR = ROOT + "/monitor";

	String MONITOR_STATUS = MONITOR + "/status";

	String PROFILE = ROOT + "/profile";

}
