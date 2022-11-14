package com.desertskyrangers.flightdeck.adapter.web;

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
	String PUBLIC_DASHBOARD = API + "/dashboard";

	String FLIGHT = API + "/flight";

	String GROUP = API + "/group";
	String GROUP_AVAILABLE = GROUP + "/available";
	String GROUP_INVITE = GROUP + "/invite";
	String GROUP_MEMBERSHIP = GROUP + "/membership";

	String LOCATION = API + "/location";

	String MEMBERSHIP = API + "/membership";

	String MONITOR = API + "/monitor";
	String MONITOR_STATUS = MONITOR + "/status";

	String PROFILE = API + "/profile";

	String LOOKUP = API + "/lookup";
	String AIRCRAFT_STATUS = LOOKUP + "/aircraft/status";
	String AIRCRAFT_TYPE = LOOKUP + "/aircraft/type";
	String BATTERY_CONNECTOR = LOOKUP + "/battery/connector";
	String BATTERY_STATUS = LOOKUP + "/battery/status";
	String BATTERY_TYPE = LOOKUP + "/battery/type";
	String GROUP_TYPE = LOOKUP + "/group/type";
	String SMS_CARRIERS = LOOKUP + "/sms/carriers";

	String USER = API + "/user";
	String USER_AIRCRAFT = USER + "/aircraft";
	String USER_BATTERY = USER + "/battery";
	String USER_FLIGHT = USER + "/flight";
	String USER_GROUP = USER + "/group";
	String USER_LOCATION = USER + "/location";
	String USER_PREFERENCES = USER + "/preferences";
	String USER_MEMBERSHIP = USER + "/membership";

	String USER_LOOKUP = USER + "/lookup";
	String USER_AIRCRAFT_LOOKUP = USER_LOOKUP + "/aircraft";
	String USER_BATTERY_LOOKUP = USER_LOOKUP + "/battery";
	String USER_LOCATION_LOOKUP = USER_LOOKUP + "/location";
	String USER_OBSERVER_LOOKUP = USER_LOOKUP + "/observer";
	String USER_PILOT_LOOKUP = USER_LOOKUP + "/pilot";

}
