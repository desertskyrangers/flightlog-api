package com.desertskyrangers.flightdeck.adapter.api;

public interface ApiPath {

	String HOST = "https://flightdeck.desertskyrangers.org";

	String API = "/api";

	String AIRCRAFT = API + "/aircraft";

	String BATTERY = API + "/battery";

	String AUTH = API + "/auth";
	String AUTH_LOGIN = AUTH + "/login";
	String AUTH_LOGOUT = AUTH + "/logout";
	String AUTH_RECOVER = AUTH + "/recover";
	String AUTH_RESET = AUTH + "/reset";
	String AUTH_REGISTER = AUTH + "/register";
	String AUTH_RESEND = AUTH + "/resend";
	String AUTH_VERIFY = AUTH + "/verify";

	String DASHBOARD = API + "/dashboard";

	String FLIGHT = API + "/flight";

	String MONITOR = API + "/monitor";
	String MONITOR_STATUS = MONITOR + "/status";

	String PROFILE = API + "/profile";

	String LOOKUP = API + "/lookup";
	String AIRCRAFT_STATUS = LOOKUP + "/aircraft/status";
	String AIRCRAFT_TYPE = LOOKUP + "/aircraft/type";
	String BATTERY_CONNECTOR = LOOKUP + "/battery/connector";
	String BATTERY_STATUS = LOOKUP + "/battery/status";
	String BATTERY_TYPE = LOOKUP + "/battery/type";
	String SMS_CARRIERS = LOOKUP + "/sms/carriers";

	String USER = API + "/user";
	String USER_AIRCRAFT = USER + "/aircraft";
	String USER_BATTERY = USER + "/battery";
	String USER_FLIGHT = USER + "/flight";

	String USER_LOOKUP = USER + "/lookup";
	String USER_AIRCRAFT_LOOKUP = USER_LOOKUP + "/aircraft";
	String USER_BATTERY_LOOKUP = USER_LOOKUP + "/battery";
	String USER_OBSERVER_LOOKUP = USER_LOOKUP + "/observer";
	String USER_PILOT_LOOKUP = USER_LOOKUP + "/pilot";

}
