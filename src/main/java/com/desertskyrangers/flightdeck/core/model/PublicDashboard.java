package com.desertskyrangers.flightdeck.core.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors( fluent = true )
@EqualsAndHashCode( callSuper = true )
public class PublicDashboard extends Dashboard {

	private String displayName;

}
