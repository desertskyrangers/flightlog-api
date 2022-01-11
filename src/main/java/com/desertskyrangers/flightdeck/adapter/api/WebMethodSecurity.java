package com.desertskyrangers.flightdeck.adapter.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity( prePostEnabled = true, securedEnabled = true, jsr250Enabled = true )
public class WebMethodSecurity extends GlobalMethodSecurityConfiguration {}
