package com.desertskyrangers.flightdeck.core.model;

public interface PreferenceKey {

	/**
	 * Preference to show the user's observer stats on their private dashboard.
	 * Valid values are "true" and "false".
	 */
	String SHOW_OBSERVER_STATS = "showObserverStats";

	/**
	 * Preference to show the user's aircraft stats on their private dashboard.
	 * Valid values are "true" and "false".
	 */
	String SHOW_AIRCRAFT_STATS = "showAircraftStats";

	/**
	 * Preference to enable the user's public dashboard. Valid values are "true"
	 * and "false".
	 */
	String ENABLE_PUBLIC_DASHBOARD = "enablePublicDashboard";

	/**
	 * Preference to show the user's observer stats on their public dashboard.
	 * Valid values are "true" and "false".
	 */
	String SHOW_PUBLIC_OBSERVER_STATS = "showPublicObserverStats";

	/**
	 * Preference to show the user's aircraft stats on their public dashboard.
	 * Valid values are "true" and "false".
	 */
	String SHOW_PUBLIC_AIRCRAFT_STATS = "showPublicAircraftStats";

	/**
	 * How many flights to show in the flight list view. Valid values are "month",
	 * "week", "day" or any positive integer.
	 */
	String FLIGHT_LIST_VIEW = "flightListView";

	/**
	 * The preference to show observer flights in the user's flight list.
	 */
	String SHOW_OBSERVER_FLIGHTS = "showObserverFlights";

	/**
	 * The preference to show owner flights in the user's flight list.
	 */
	String SHOW_OWNER_FLIGHTS = "showOwnerFlights";

}
