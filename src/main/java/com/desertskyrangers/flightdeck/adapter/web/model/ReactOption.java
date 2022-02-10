package com.desertskyrangers.flightdeck.adapter.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true )
@NoArgsConstructor
@AllArgsConstructor
public class ReactOption {

	private String id;

	private String name;

}
