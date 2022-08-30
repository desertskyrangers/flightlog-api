package com.desertskyrangers.flightdeck.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatCollector<T> {

	private final Map<String, List<OwnedValue<T>>> statMap;

	public StatCollector() {
		statMap = new HashMap<>();
	}

	public void add( String key, T owner, double value ) {
		statMap.computeIfAbsent( key, k -> new ArrayList<>() ).add( new OwnedValue<>( owner, value ) );
	}

	public StatSeries<T> get( String key ) {
		return new StatSeries<>( statMap.get( key ) );
	}

}
