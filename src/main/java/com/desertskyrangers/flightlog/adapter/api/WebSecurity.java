package com.desertskyrangers.flightlog.adapter.api;

import com.desertskyrangers.flightlog.adapter.api.jwt.JwtFilter;
import com.desertskyrangers.flightlog.adapter.api.jwt.JwtTokenProvider;
import com.desertskyrangers.flightlog.core.AppPrincipalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final AppPrincipalService appPrincipalService;

	private final JwtTokenProvider jwtTokenProvider;

	public WebSecurity( AppPrincipalService appPrincipalService, JwtTokenProvider jwtTokenProvider ) {
		this.appPrincipalService = appPrincipalService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public UserDetailsService userDetailsServiceBean() {
		return appPrincipalService;
	}

	@Override
	protected void configure( AuthenticationManagerBuilder builder ) throws Exception {
		builder.userDetailsService( appPrincipalService ).passwordEncoder( passwordEncoder() );
	}

	@Override
	protected void configure( HttpSecurity http ) throws Exception {
		// @formatter:off
		http
			.cors().and()
			.csrf().ignoringAntMatchers( ApiPath.AUTH_LOGIN ).and()
			.csrf().ignoringAntMatchers( ApiPath.AUTH_REGISTER ).and()
			.authorizeRequests()
				.mvcMatchers( HttpMethod.POST, ApiPath.AUTH_LOGIN ).permitAll()
				.mvcMatchers( HttpMethod.POST, ApiPath.AUTH_REGISTER ).permitAll()
				.mvcMatchers( HttpMethod.GET, ApiPath.AUTH_VERIFY ).permitAll()
				.mvcMatchers( HttpMethod.GET, ApiPath.MONITOR_STATUS ).permitAll()
				.anyRequest().authenticated()
			.and().addFilterAfter( new JwtFilter( jwtTokenProvider ), UsernamePasswordAuthenticationFilter.class );
		// @formatter:on
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		//		configuration.setAllowedOrigins( List.of( "*" ) );
		//		configuration.setAllowedMethods( List.of( "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH" ) );
		//		// setAllowCredentials(true) is important, otherwise:
		//		// The value of the 'Access-Control-Allow-Origin' header in the response must
		//		// not be the wildcard '*' when the request's credentials mode is 'include'.
		//		configuration.setAllowCredentials( true );
		//		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		//		// will fail with 403 Invalid CORS request
		//		configuration.setAllowedHeaders( List.of( "Authorization", "Cache-Control", "Content-Type" ) );
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration( "/**", configuration );
		return source;
	}

}
