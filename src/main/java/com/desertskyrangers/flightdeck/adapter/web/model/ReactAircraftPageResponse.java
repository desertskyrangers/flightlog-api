package com.desertskyrangers.flightdeck.adapter.web.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Accessors( chain = true )
public class ReactAircraftPageResponse {

	private Page<ReactAircraft> aircraft;

	private List<String> messages;

}
