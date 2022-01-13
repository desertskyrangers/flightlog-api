package com.desertskyrangers.flightdeck.adapter.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( chain = true )
public class ReactBatteryPageResponse {

	private List<ReactBattery> batteries;

	private List<String> messages;

}
