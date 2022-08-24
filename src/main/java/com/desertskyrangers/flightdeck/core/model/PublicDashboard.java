package com.desertskyrangers.flightdeck.core.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @deprecated Just use the normal dashboard class
 */
@Data
@Accessors( fluent = true )
@EqualsAndHashCode( callSuper = true )
@Deprecated
public class PublicDashboard extends Dashboard {

	private String displayName;

}
