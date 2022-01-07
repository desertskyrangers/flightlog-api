package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.model.ReactSmsCarrier;
import com.desertskyrangers.flightlog.core.model.SmsCarrier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class LookupController {

	@GetMapping( path = ApiPath.SMS_CARRIERS )
	ResponseEntity<List<ReactSmsCarrier>> smsCarriers() {
		return new ResponseEntity<>( Arrays.stream( SmsCarrier.values() ).map( c -> new ReactSmsCarrier( c.name().toLowerCase(), c.getTitle() ) ).toList(), HttpStatus.OK );
	}

}
