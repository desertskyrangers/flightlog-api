package com.desertskyrangers.flightlog.plug.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class JsonModel {
	public static final JsonModel EMPTY = new JsonModel();
}
