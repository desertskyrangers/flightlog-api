package com.desertskyrangers.flightlog.adapter.api;

public interface ApiPath {

	String HOST = "https://flightlog.desertskyrangers.com";

	String API = "/api";

	String AUTH = API + "/auth";

	String AUTH_REGISTER = AUTH + "/register";

	String AUTH_VERIFY = AUTH + "/verify";

	String AUTH_RESEND = AUTH + "/resend";

	String AUTH_LOGIN = AUTH + "/login";

	String AUTH_LOGOUT = AUTH + "/logout";

	String MONITOR = API + "/monitor";

	String MONITOR_STATUS = MONITOR + "/status";

	String PROFILE = API + "/profile";

}
