package com.desertskyrangers.flightdeck.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors( fluent = true )
public class Dashboard {
    private UUID id = UUID.randomUUID();

    private int flightCount;
    private long flightTime;
}
