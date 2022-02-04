package com.desertskyrangers.flightdeck.core.exception;

import com.desertskyrangers.flightdeck.core.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = true )
public class UnauthorizedException extends AppSecurityException {

	private final User user;

}
