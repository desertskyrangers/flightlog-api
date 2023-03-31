package com.desertskyrangers.flightdeck.util;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class Template {

	public static String fill( String template, Map<String, Object> values ) {
		PebbleEngine engine = new PebbleEngine.Builder().build();
		PebbleTemplate compiledTemplate = engine.getTemplate( template );

		StringWriter writer = new StringWriter();
		try {
			compiledTemplate.evaluate( writer, values );
		} catch( IOException exception ) {
			log.error( "Unable to process email template", exception );
			return null;
		}
		return writer.toString();
	}

}
